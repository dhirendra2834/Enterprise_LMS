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

	// registration 
	public UserResponseDTO registration(User user) {
		System.out.println("user Service is called");

	    // Check whether user already exists
	    Optional<User> userExists = userRepository.findByEmail(user.getEmail());

	    if (userExists.isPresent()) {
	        throw new RuntimeException("User already exists with this email");
	    }
  
	   User savedUser=User.builder()
			   .name(user.getName())
			   .email(user.getEmail())
			   .password(passwordEncoder.encode(user.getPassword()))
		       .roles(Set.of(Role.STUDENT))
		       .phoneNumber(user.getPhoneNumber())
			   .build();

        // Save data in DB
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
	
//	 this method is called and add by Admin or Adminitration
	public UserResponseDTO addUser(User user, Long id) {

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
			       .createdBy(id)
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

	public UserResponseDTO login(LoginRequestDTO loginRequestDto) {
		System.out.println("login method is running "+loginRequestDto.getEmail()+ " "+ loginRequestDto.getPassword());
		 
           // check Authentication
		   Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail() , loginRequestDto.getPassword())
	        );


	               
	      System.out.println("login method is running1 ");
         //	  Get data 
	      User existingUser = (User) authentication.getPrincipal();

	      System.out.println("login method is running2 ");
		 // Generate JWT using user email and roles
	    String token = jwtUtil.generateToken(
	    		existingUser.getEmail(),
	    		existingUser.getRoles()
	    );
	    System.out.println("login method is running3 ");
		 UserResponseDTO userResponseDTO =
		            UserResponseDTO.builder()
		                    .message("User registered successfully")
		                    .Id(existingUser.getId())
		                    .email(existingUser.getEmail())
		                    .name(existingUser.getName())
		                    .jwttoken(token)
		                    .roles(existingUser.getRoles())
		                    .build();
		 
			System.out.println("login method is end ");


		    return userResponseDTO;
		
	}

	public Optional<User> getUserDataById(int id) {
		Optional<User> userexits= userRepository.findById(id);
		if(!userexits.isPresent()) {
			   throw new RuntimeException("User does not exists with this email");
		}
		return userexits;
	}

	public List<User> getAllUserData() {
		List<User> allUserData= userRepository.findAll();
		return allUserData;
	}
	
	
}
