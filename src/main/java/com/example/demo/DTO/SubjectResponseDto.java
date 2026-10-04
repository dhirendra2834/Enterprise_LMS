package com.example.demo.DTO;

import java.util.List;

import com.example.demo.model.Chapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubjectResponseDto {

	    private Integer id;
	    private String name;
	    private String description;	    
	    private List<Subject_CourseDto> courses;   
	    private List<Subject_ChapterDto> chapters;
  
}
