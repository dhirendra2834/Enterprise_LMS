package com.example.demo.DTO.PaymentDTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

    @NotNull
    private Integer courseId;
}