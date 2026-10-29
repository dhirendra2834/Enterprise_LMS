package com.example.demo.DTO.PaymentDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreatePaymentResponse {

    private Integer paymentRecordId;
    private String razorpayOrderId;
    private String keyId;
    private Long amount;
    private String currency;
    private Integer courseId;
}