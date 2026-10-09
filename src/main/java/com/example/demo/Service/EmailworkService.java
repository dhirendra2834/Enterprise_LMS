package com.example.demo.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ForgetPasswordRequestDto;
import com.example.demo.model.PasswordResetOTP;
import com.example.demo.model.User;
import com.example.demo.repository.EmailworkRepositroy;
import com.example.demo.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailworkService {

    private final EmailworkRepositroy emailworkRepositroy;
    private final JavaMailSender javaMailSender;
    
    private final UserRepository userRepository;

    // Forgot password: Send OTP to email
    public String forgetPasswordSentOtp(ForgetPasswordRequestDto forgetPasswordRequestDto) {

    	  Optional<User> user= userRepository.findByEmail(forgetPasswordRequestDto.getEmail());
    	  
    	  if(user.isEmpty()) {
    		  throw new Error("User is not found ");
    	  }
        // 1. Generate 6-digit OTP
        String otp = String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 1000000)
        );

        // 2. Create email message
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(forgetPasswordRequestDto.getEmail());
        message.setSubject("Password Reset OTP");

        message.setText(
                "Your OTP for password reset is: " + otp
                + "\n\nThis OTP is valid for 10 minutes."
                + "\n\nIf you did not request this, ignore this email."
        );

        // 3. Send email
        javaMailSender.send(message);
        
        PasswordResetOTP passwordResetOTP= 
        		PasswordResetOTP.builder()
        		.otp(otp)
        		.user(user.get())
        		.build();
        
        emailworkRepositroy.save(passwordResetOTP);

        // 4. Return response
        return "OTP sent successfully to your email";
    }
}
