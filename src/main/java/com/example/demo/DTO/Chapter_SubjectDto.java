package com.example.demo.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Chapter_SubjectDto {


    private Integer id;
    private String name;
    private String description;	    
    private List<Subject_CourseDto> courses;   
}
