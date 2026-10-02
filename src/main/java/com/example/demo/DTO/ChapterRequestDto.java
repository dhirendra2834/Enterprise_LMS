package com.example.demo.DTO;

import lombok.Data;

@Data
public class ChapterRequestDto {

	    private String name;
	    private String description;
	    private Integer chapterOrder;
	    private Integer subjectId;
}
