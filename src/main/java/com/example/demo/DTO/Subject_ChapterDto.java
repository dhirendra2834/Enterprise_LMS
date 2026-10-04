package com.example.demo.DTO;

import com.example.demo.model.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Subject_ChapterDto {
	  private Integer id;
	  private String name;
	  private String description;
	  private Integer chapterOrder;
}
