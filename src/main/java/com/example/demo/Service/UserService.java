package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.LoginRequestDTO;
import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.model.Type.Role;
import com.example.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
   
	private UserRepository userRepository;
	
	private JwtUtil jwtUtil;
	
	private PasswordEncoder passwordEncoder;
	
	private AuthenticationManager authenticationManager;
	
//   for Add user
//	 this method is called and add by Admin or Adminitration
	public UserResponseDTO addUser(User user, Authentication authentication) {
  
         // get login User Details
		User loggedInUser = (User) authentication.getPrincipal();
		
	    // Check whether user already exists
	    Optional<User> userExists = userRepository.findByEmail(user.getEmail());

	    if (userExists.isPresent()) {
	        throw new RuntimeException("User already exists with this email");
	    }

	    User savedUser=User.builder()
				   .name(user.getName())
				   .email(user.getEmail())
				   .password(passwordEncoder.encode(user.getPassword()))
			       .roles(user.getRoles())
			       .createdBy(loggedInUser.getId())
			       .phoneNumber(user.getPhoneNumber())
				   .build();

	     
	    userRepository.save(savedUser);
	    // Generate JWT using saved user's roles
	    String token = jwtUtil.generateToken(
	            savedUser.getEmail(),
	            savedUser.getRoles()
	    );

	    // Create response
	    UserResponseDTO userResponseDTO =
	            UserResponseDTO.builder()
	                    .message("User registered successfully")
	                    .Id(savedUser.getId())
	                    .email(savedUser.getEmail())
	                    .name(savedUser.getName())
	                    .jwttoken(token)
	                    .roles(savedUser.getRoles())
	                    .build();

	    return userResponseDTO;
	}

	public Optional<User> getUserDataById(int id) {
		Optional<User> userexits= userRepository.findById(id);
		if(!userexits.isPresent()) {
			   throw new RuntimeException("User does not exists with this email");
		}
		return userexits;
	}
	
	public UserResponseDTO getSelfDetails(Authentication authentication) {
		User loggedInUser = (User) authentication.getPrincipal();
		
		Optional<User> userDetails= userRepository.findById(loggedInUser.getId());
		if(userDetails.isEmpty()) {
			throw new Error("user is not login/exits");
		}
		
		User userData= userDetails.get();
		
		  UserResponseDTO userResponseDTO =
		            UserResponseDTO.builder()
		                    .message("This is your self details")
		                    .Id(userData.getId())
		                    .email(userData.getEmail())
		                    .name(userData.getName())
		                    .roles(userData.getRoles())
		                    .build();

		    return userResponseDTO;
		
		
	}

	public List<User> getAllUserData() {
		List<User> allUserData= userRepository.findAll();
		return allUserData;
	}
	
	
}
