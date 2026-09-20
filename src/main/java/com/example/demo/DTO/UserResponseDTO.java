package com.example.demo.DTO;

import java.util.Set;

import com.example.demo.model.Type.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
	
    private String message;
    private String name;
    private String email;
    private String jwttoken;
    private Set<Role> roles;
}
