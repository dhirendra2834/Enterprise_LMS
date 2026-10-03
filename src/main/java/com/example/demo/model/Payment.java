package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.model.Type.PaymentMethod;
import com.example.demo.model.Type.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Payment {

	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "course_id", nullable = false)
	    private Course course;

	    private BigDecimal amount;

	    private String currency;

	    @Enumerated(EnumType.STRING)
	    private PaymentMethod paymentMethod;

	    @Enumerated(EnumType.STRING)
	    private PaymentStatus status;

	    // For online gateway
	    private String gatewayOrderId;

	    private String gatewayPaymentId;

	    // For offline payment
	    private String transactionReference;

	    private LocalDateTime createdAt;

	    private LocalDateTime paidAt;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "recorded_by")
	    private User recordedBy;
}
