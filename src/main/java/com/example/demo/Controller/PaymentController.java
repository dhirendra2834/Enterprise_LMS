package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.PaymentDTOS.CreatePaymentRequest;
import com.example.demo.DTO.PaymentDTOS.CreatePaymentResponse;
import com.example.demo.DTO.PaymentDTOS.VerifyPaymentRequest;
import com.example.demo.Service.PaymentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<CreatePaymentResponse> createOrder(
            @Valid @RequestBody CreatePaymentRequest request,
            Authentication authentication) throws Exception {

        String email = authentication.getName();

        return ResponseEntity.ok(
                paymentService.createOrder(email, request));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request,
            Authentication authentication) throws Exception {

        String email = authentication.getName();

        return ResponseEntity.ok(
                paymentService.verifyPayment(email, request));
    }
}