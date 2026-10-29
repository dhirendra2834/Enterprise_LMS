package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Payment;
import com.example.demo.model.User;

public interface PaymentRepository
        extends JpaRepository<Payment, Integer> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);


}