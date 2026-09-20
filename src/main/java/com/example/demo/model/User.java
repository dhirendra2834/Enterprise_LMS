package com.example.demo.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.Type.Role;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
  
    @Column(nullable = false)
    private String email;
 
    @Column(nullable = false)
    private String password;
     
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    // Audit information
    private Long createdBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

//     AUthority added here 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(
                        "ROLE_" + role.name()
                ))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }
}