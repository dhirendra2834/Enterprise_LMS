package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.Chapter;
import com.example.demo.model.Course;
import com.example.demo.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubjectResponseDto {

	    private Integer id;
	    private String name;
	    private String description;	    
	    private List<Course> courses;   
	    private List<Chapter> chapters;
  
}
