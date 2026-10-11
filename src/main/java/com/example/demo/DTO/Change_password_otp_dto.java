package com.example.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Change_password_otp_dto {
  
	 @NotBlank(message = "Email is required")
	    @Email(message = "Please provide a valid email address")
	    private String email;

	    @NotBlank(message = "Password is required")
	    @Size(min = 8, message = "Password must contain at least 8 characters")
	    private String oldPassword;
	    
	    @NotBlank(message = "Password is required")
	    @Size(min = 8, message = "Password must contain at least 8 characters")
	    private String newPassword;
}
