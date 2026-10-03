package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.model.User;
import com.example.demo.model.Type.EnrollmentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponseDto {

	 private Integer id;

	  private User user;

	  private Integer courseId;

	  private String courseName;

	  private LocalDateTime enrolledAt;

	  private EnrollmentStatus status;

	  private Double progress;
}
