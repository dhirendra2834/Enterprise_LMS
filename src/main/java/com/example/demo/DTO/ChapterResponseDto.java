package com.example.demo.DTO;

import com.example.demo.model.Subject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChapterResponseDto {

	  private Integer id;
	  private String name;
	  private String description;
	  private Integer chapterOrder;
	  private Chapter_SubjectDto subject;

}
