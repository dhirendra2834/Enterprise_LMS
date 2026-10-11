package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.demo.model.PasswordResetOTP;
import com.example.demo.model.User;

@Repository
public interface EmailworkRepositroy extends JpaRepository<PasswordResetOTP, Integer>{
	
    Optional<PasswordResetOTP> findTopByUserOrderByCreatedAtDesc(User user);
    
    void deleteByUserEmail(String email);
    
    Optional<PasswordResetOTP> findByUser_Id(Integer userId);

}
