package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.Change_password_otp_dto;
import com.example.demo.DTO.ForgetPasswordRequestDto;
import com.example.demo.DTO.LoginRequestDTO;
import com.example.demo.DTO.ResetPasswordRequestDto;
import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.DTO.VerifyOtpRequestDto;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.EmailworkService;
import com.example.demo.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")

//POST /auth/forgot-password         
//POST /auth/verify-otp            
//POST /auth/reset-password        
//POST /auth/logout   
public class AuthController {

	private AuthService authService;
	private EmailworkService emailworkService;

//	Registration
	@PostMapping("/signup")
	public ResponseEntity<UserResponseDTO> signup(@RequestBody User user) {
		System.out.println("user Controller is called");
		UserResponseDTO userResponseDTO = authService.registration(user);
		return ResponseEntity.ok(userResponseDTO);
	}

//	 Login
	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO user) {
		UserResponseDTO userResponseDTO = authService.login(user);
		return ResponseEntity.ok(userResponseDTO);
	}

//	forget password
	@PostMapping("/forget_password")
	public ResponseEntity<String> forgetpassword(@RequestBody ForgetPasswordRequestDto forgetPasswordRequestDto) {
		String response = emailworkService.forgetPasswordSentOtp(forgetPasswordRequestDto);
		return ResponseEntity.ok(response);
	}

//	verify OTP
	@PostMapping("/verify_otp")
	public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequestDto verifyOtpRequestDto) {
         System.out.println("verify Otp controller");
		String isOtpVerified = emailworkService.verifyOtp(verifyOtpRequestDto.getEmail(), verifyOtpRequestDto.getOtp());
		return ResponseEntity.ok(isOtpVerified);
	}
	
//	Reset Password
	@PostMapping("/reset_password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto requestDto){
		String sucessfullyChangedPassword= emailworkService.resetPassword(requestDto);
		return ResponseEntity.ok(sucessfullyChangedPassword);
	}
	
//	change password
	@PostMapping("/change_password")
	public ResponseEntity<String> changePassword(@RequestBody Change_password_otp_dto change_password_otp_dto){
		String isPasswordChanged= emailworkService.changePassword(change_password_otp_dto);
		return ResponseEntity.ok(isPasswordChanged);
	}

}
