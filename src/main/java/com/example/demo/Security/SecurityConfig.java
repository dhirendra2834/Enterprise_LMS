package com.example.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// used for Disable third party hit the backend
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
				        "/v3/api-docs/**",
				        "/v3/api-docs.yaml",
				        "/swagger-ui/**",
				        "/swagger-ui.html"
				    ).permitAll()
						.requestMatchers("/auth/**").permitAll()
						// Role based API end Point filtering
						.requestMatchers("/user/create/**", "/user/get/**", "/user/get/all","/courses")
						.hasAnyRole("ADMIN", "ADMINITRATION").anyRequest().authenticated())
				
				// this line make from Stateful to stateless
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Before UsernamePasswordAuth filter keep JWTfilter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build(); // its make filterchain
																									// Authentication //
																									// // object
	}

// Used for Authentication provides 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

// Used for Password Bcrypt 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
