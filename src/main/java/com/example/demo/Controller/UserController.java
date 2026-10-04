package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.LoginRequestDTO;
import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.Service.UserService;
import com.example.demo.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

//	 create user by Admin or Administration 
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PostMapping("/create")
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody User user, Authentication authentication) {
		UserResponseDTO userResponseDTO = userService.addUser(user,authentication);
		return ResponseEntity.ok(userResponseDTO);
	}
	
//	get User Details (Self profile)
	@GetMapping("/me")
	public ResponseEntity<UserResponseDTO> getselfData(Authentication authentication){
		UserResponseDTO userDetails = userService.getSelfDetails(authentication);
		return ResponseEntity.ok(userDetails);

	}
	
//	 get User details By UserId
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@GetMapping("/get/{Id}")
	public ResponseEntity<Optional<User>> getUserDataById(@PathVariable int Id){
		Optional<User> user = userService.getUserDataById(Id);
		return ResponseEntity.ok(user);
	}
//	get All User list 
	@GetMapping("/get/all")
	public ResponseEntity<List<User>> getAllUserData(){
		List<User> user = userService.getAllUserData();
		return ResponseEntity.ok(user);
	}

}
