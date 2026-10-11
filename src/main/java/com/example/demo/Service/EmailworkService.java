package com.example.demo.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.Change_password_otp_dto;
import com.example.demo.DTO.ForgetPasswordRequestDto;
import com.example.demo.DTO.ResetPasswordRequestDto;
import com.example.demo.model.PasswordResetOTP;
import com.example.demo.model.User;
import com.example.demo.repository.EmailworkRepositroy;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailworkService {

    private final EmailworkRepositroy emailworkRepositroy;
    private final JavaMailSender javaMailSender;
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    // Forgot password: Send OTP to email

    public String forgetPasswordSentOtp(ForgetPasswordRequestDto forgetPasswordRequestDto) {

    	  Optional<User> user= userRepository.findByEmail(forgetPasswordRequestDto.getEmail());
    	  
    	  if(user.isEmpty()) {
    		  return "User is not found ";
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
        
        //4.  Delete the previous OTP record for this user
        emailworkRepositroy.deleteByUserEmail(user.get().getEmail());
        
        PasswordResetOTP passwordResetOTP= 
        		PasswordResetOTP.builder()
        		.otp(passwordEncoder.encode(otp))
        		.user(user.get())
        		.build();
        
        emailworkRepositroy.save(passwordResetOTP);

        // 5. Return response
        return "OTP sent successfully to your email";
    }
    
//    verify otp 
//    @Transactional
    public String verifyOtp(String email, String otp) {
    	 System.out.println("verify Otp service");
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
        	return "Invalid Email";
        }
                

        Optional<PasswordResetOTP> resetOTP = emailworkRepositroy
                .findTopByUserOrderByCreatedAtDesc(user.get());
         
        if(resetOTP.isEmpty()) {
        	return "OTP not found";
        }

        if (resetOTP.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return "OTP has expired";
        }

        if (resetOTP.get().isVerified()) {
            return "OTP already verified";
        }

        if (resetOTP.get().getAttempts() >= 5) {
            return "Maximum OTP attempts exceeded";
        }

        if (!passwordEncoder.matches(otp, resetOTP.get().getOtp())) {
            resetOTP.get().setAttempts(resetOTP.get().getAttempts() + 1);
            emailworkRepositroy.save(resetOTP.get());
            return "Invalid Otp";
        }

        resetOTP.get().setVerified(true);
        emailworkRepositroy.save(resetOTP.get());
        System.out.println("verify Otp service end");
        
        return "OTP verified Successfully";
    }

	public String resetPassword(ResetPasswordRequestDto requestDto) {
		
		Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
		
		if(user.isEmpty()) {
			return "this email is not registered from any User details";
		}
		 System.out.println("User id here "+user.get().getId());
//		Optional<PasswordResetOTP> passwordResetOTP=emailworkRepositroy.findById(user.get().getId());
	Optional<PasswordResetOTP> passwordResetOTP=emailworkRepositroy.findByUser_Id(user.get().getId());

		
		if(passwordResetOTP.isEmpty()) {
			return "OTP is not exits in the behalf of this user";
		}
		
		if(!passwordResetOTP.get().isVerified()) {
			return "your OTP is not verified, please check it ";
		}
		
		User userDetails= user.get();
		
		userDetails.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		userRepository.save(userDetails);
		
		return "password Changed Sucessfully";
	}

	public String changePassword(Change_password_otp_dto change_password_otp_dto) {
		
		Optional<User> user= userRepository.findByEmail(change_password_otp_dto.getEmail());
		
		if(user.isEmpty()) {
			return "user is not exits";
		}
		
		String oldPassword= change_password_otp_dto.getOldPassword();
		String newPassword= change_password_otp_dto.getNewPassword();
		
		User userDetails= user.get();
		
		if (!passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
		    return "Your old password is not correct";
		}
		
		userDetails.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(userDetails);
	
		
		return "your password is changed successfully";
	}
}
