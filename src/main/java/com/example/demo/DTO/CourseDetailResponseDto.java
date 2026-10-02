package com.example.demo.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.Subject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseDetailResponseDto {

	private Integer id;
	
	private String name;
	private String description;
	
	private List<Subject> subjects;
    
	private BigDecimal fees;
}
