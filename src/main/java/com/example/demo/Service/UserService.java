package com.example.demo.Service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.model.Type.Role;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
   
	@Autowired
	private UserRepository userRepository;
	private JwtUtil jwtUtil;

	// registration 
	public UserResponseDTO registration(User user) {

	    // Check whether user already exists
	    Optional<User> userExists = userRepository.findByEmail(user.getEmail());

	    if (userExists.isPresent()) {
	        throw new RuntimeException("User already exists with this email");
	    }

	    // New users get STUDENT role by default
	    user.setRoles(Set.of(Role.STUDENT));

	    // Save user
	    User savedUser = userRepository.save(user);

	    // Generate JWT using saved user's roles
	    String token = jwtUtil.generateToken(
	            savedUser.getEmail(),
	            savedUser.getRoles()
	    );

	    // Create response
	    UserResponseDTO userResponseDTO =
	            UserResponseDTO.builder()
	                    .message("User registered successfully")
	                    .email(savedUser.getEmail())
	                    .name(savedUser.getName())
	                    .jwttoken(token)
	                    .roles(savedUser.getRoles())
	                    .build();

	    return userResponseDTO;
	}
	
	public UserResponseDTO addUser(User user) {

	    // Check whether user already exists
	    Optional<User> userExists = userRepository.findByEmail(user.getEmail());

	    if (userExists.isPresent()) {
	        throw new RuntimeException("User already exists with this email");
	    }


	    // Save user
	    User savedUser = userRepository.save(user);

	    // Generate JWT using saved user's roles
	    String token = jwtUtil.generateToken(
	            savedUser.getEmail(),
	            savedUser.getRoles()
	    );

	    // Create response
	    UserResponseDTO userResponseDTO =
	            UserResponseDTO.builder()
	                    .message("User registered successfully")
	                    .email(savedUser.getEmail())
	                    .name(savedUser.getName())
	                    .jwttoken(token)
	                    .roles(savedUser.getRoles())
	                    .build();

	    return userResponseDTO;
	}

	public UserResponseDTO login(User user) {
		Optional<User> userExits= userRepository.findByEmail(user.getEmail());
		
		if(!userExits.isPresent()) {
			 throw new RuntimeException("User not exists with this email");
		}
		// Get the actual user from database
	    User existingUser = userExits.get();
		
		 // Generate JWT using user email and roles
	    String token = jwtUtil.generateToken(
	    		existingUser.getEmail(),
	    		existingUser.getRoles()
	    );
		
		 UserResponseDTO userResponseDTO =
		            UserResponseDTO.builder()
		                    .message("User registered successfully")
		                    .email(existingUser.getEmail())
		                    .name(existingUser.getName())
		                    .jwttoken(token)
		                    .roles(existingUser.getRoles())
		                    .build();

		    return userResponseDTO;
		
	}
	
	
	
}
