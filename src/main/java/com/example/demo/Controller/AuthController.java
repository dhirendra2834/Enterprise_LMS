package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.LoginRequestDTO;
import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.Service.AuthService;
import com.example.demo.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthService authService;

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
	
}
