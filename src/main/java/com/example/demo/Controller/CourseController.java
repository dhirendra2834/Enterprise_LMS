package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.CourseDetailResponseDto;
import com.example.demo.Service.CourseService;
import com.example.demo.model.Course;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@AllArgsConstructor
@Controller
@RequestMapping("/courses")
public class CourseController {

	private CourseService courseService;
//	create Course 
	@PostMapping
	public ResponseEntity<String> createCourse(@RequestBody Course course) {
		System.out.println("course controller running ");
		String courseCreated=courseService.createCourse(course);
		return ResponseEntity.ok(courseCreated);
	}

//	 get Course By id
	@GetMapping("/{Id}")
	public ResponseEntity<CourseDetailResponseDto> getCourseById(@PathVariable int Id){
		CourseDetailResponseDto course=courseService.getCourseById(Id);
		return ResponseEntity.ok(course);
	}

//	get ALl courses
	@GetMapping
	public List<CourseDetailResponseDto> getAllCourses(){
		List<CourseDetailResponseDto> courses= courseService.getAllCourses();
		return courses;
	}
	
//	Update the course by course_id
	@PutMapping("{id}")
	public String putCourseUpdate(@PathVariable Course course, int Id) {
    return courseService.updateCourse(course, Id);
	}
	
//	Delete the course by course_Id
	@DeleteMapping("{id}")
	public String deleteCourseById(@PathVariable int Id) {
		return courseService.deleteCourseById(Id);
	}
	
//	delete All course
	public String deleteAllCourses() {
		return courseService.deleteAllCourses();
	}
	
}
