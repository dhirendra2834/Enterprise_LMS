package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Service.CourseService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/course")
public class CourseController {

	private final CourseService courseService;
	
	
}
