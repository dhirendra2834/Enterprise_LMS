package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.demo.model.PasswordResetOTP;

@Repository
public interface EmailworkRepositroy extends JpaRepository<PasswordResetOTP, Long>{

}
