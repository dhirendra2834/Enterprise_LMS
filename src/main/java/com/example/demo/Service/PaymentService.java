package com.example.demo.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DTO.PaymentDTOS.CreatePaymentRequest;
import com.example.demo.DTO.PaymentDTOS.CreatePaymentResponse;
import com.example.demo.DTO.PaymentDTOS.VerifyPaymentRequest;
import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.model.Type.PaymentStatus;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public CreatePaymentResponse createOrder(
            String email,
            CreatePaymentRequest request) throws Exception {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        // Do not create another purchase if already enrolled.
        if (enrollmentRepository
                .existsByUserIdAndCourseId(user.getId(), course.getId())) {
            throw new RuntimeException("Already enrolled in this course");
        }

        if (course.getFees() == null ||
                course.getFees().signum() <= 0) {
            throw new RuntimeException("Invalid course fee");
        }

        // BigDecimal prevents floating-point price errors.
        long amountInPaise = course.getFees()
                .movePointRight(2)
                .longValueExact();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt",
                "course_" + course.getId() + "_user_" + user.getId()
                        + "_" + System.currentTimeMillis());

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setAmountInPaise(amountInPaise);
        payment.setCurrency("INR");
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
        
        CreatePaymentResponse createPaymentResponse= CreatePaymentResponse.builder()
        		.paymentRecordId(payment.getId())
        		.razorpayOrderId(payment.getRazorpayOrderId())
        		.keyId(keyId)
        		.amount(payment.getAmountInPaise())
        		.currency(payment.getCurrency())
        		.courseId(payment.getCourse().getId())
        		.build();

        return createPaymentResponse;
    }

    @Transactional
    public String verifyPayment(
            String email,
            VerifyPaymentRequest request) throws Exception {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Load the order from our database, not from frontend data alone.
        Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Payment order not found"));

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Payment does not belong to this user");
        }

        // Idempotency: a repeated callback must not create another enrollment.
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return "Payment already verified";
        }

        String payload = payment.getRazorpayOrderId()
                + "|" + request.getRazorpayPaymentId();

        String expectedSignature = hmacSha256(payload, keySecret);

        if (!MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                request.getRazorpaySignature()
                        .getBytes(StandardCharsets.UTF_8))) {
            throw new RuntimeException("Invalid payment signature");
        }

        // Check the gateway's payment status and amount, not just the
        // browser's success callback.
        com.razorpay.Payment gatewayPayment =
                razorpayClient.payments.fetch(
                        request.getRazorpayPaymentId());

        String gatewayOrderId = gatewayPayment.get("order_id");
        String gatewayStatus = gatewayPayment.get("status");
        Number gatewayAmount = gatewayPayment.get("amount");

        if (!payment.getRazorpayOrderId().equals(gatewayOrderId)) {
            throw new RuntimeException("Payment order mismatch");
        }

        if (gatewayAmount == null ||
                gatewayAmount.longValue() != payment.getAmountInPaise()) {
            throw new RuntimeException("Payment amount mismatch");
        }

        if (!"INR".equals(gatewayPayment.get("currency"))) {
            throw new RuntimeException("Payment currency mismatch");
        }

        // This flow grants access only after the payment is captured.
        if (!"captured".equals(gatewayStatus)) {
            return "Payment is not captured yet";
        }

        payment.setRazorpayPaymentId(
                request.getRazorpayPaymentId());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Do not create a duplicate enrollment.
        if (!enrollmentRepository.existsByUserIdAndCourseId(
                user.getId(), payment.getCourse().getId())) {

            // Adapt these setters/status to your existing Enrollment entity.
            Enrollment enrollment =
                    new Enrollment();

            enrollment.setUser(user);
            enrollment.setCourse(payment.getCourse());
            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollment.setProgress(0.0);

            // Set your existing successful enrollment status here.
            // Example, if your enum contains ACTIVE:
            // enrollment.setStatus(EnrollmentStatus.ACTIVE);

            enrollmentRepository.save(enrollment);
        }

        return "Payment verified and course enrollment completed";
    }

//    this method is used for payment signature 
    private String hmacSha256(
            String payload,
            String secret) throws Exception {

        Mac mac = Mac.getInstance("HmacSHA256");

        mac.init(new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"));

        byte[] hash = mac.doFinal(
                payload.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(hash);
    }
}
