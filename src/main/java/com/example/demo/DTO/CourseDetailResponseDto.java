package com.example.demo.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseDetailResponseDto {

	private Integer id;
	
	private String name;
	private String description;
	
	private List<Course_SubjectDto> subjects;
    
	private BigDecimal fees;
	
}
