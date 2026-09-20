package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.Service.UserService;
import com.example.demo.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller

@RequestMapping("/user")
public class UserController {
   
	
	private final UserService userService;
	
//	create a user
	@PostMapping("/signup")
	public ResponseEntity<UserResponseDTO> signup(@RequestBody User user){
		UserResponseDTO userResponseDTO= userService.registration(user);
		return ResponseEntity.ok(userResponseDTO);
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO> login(@RequestBody User user){
		UserResponseDTO userResponseDTO= userService.login(user);
		return ResponseEntity.ok(userResponseDTO);
	}
//	 create user by Admin or Administration 
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PostMapping("/create")
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody User user){
		UserResponseDTO userResponseDTO= userService.addUser(user);
		return ResponseEntity.ok(userResponseDTO);
	}
	
	
}
