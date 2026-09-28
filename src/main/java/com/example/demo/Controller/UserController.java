package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.LoginRequestDTO;
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
	public ResponseEntity<UserResponseDTO> signup(@RequestBody User user) {
		System.out.println("user Controller is called");
		UserResponseDTO userResponseDTO = userService.registration(user);
		return ResponseEntity.ok(userResponseDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO user) {
		UserResponseDTO userResponseDTO = userService.login(user);
		return ResponseEntity.ok(userResponseDTO);
	}

//	 create user by Admin or Administration 
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PostMapping("/create/{id}")
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody User user, @PathVariable Long id) {
		UserResponseDTO userResponseDTO = userService.addUser(user,id);
		return ResponseEntity.ok(userResponseDTO);
	}
	
//	 get 1 User details
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
