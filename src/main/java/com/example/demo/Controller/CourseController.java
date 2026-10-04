package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CourseDetailResponseDto;
import com.example.demo.Service.CourseService;
import com.example.demo.model.Course;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

	private CourseService courseService;
//	create Course 
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PostMapping
	public ResponseEntity<String> createCourse(@RequestBody Course course) {
		System.out.println("course controller running ");
		String courseCreated=courseService.createCourse(course);
		return ResponseEntity.ok(courseCreated);
	}

//	 get Course By id
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
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
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PutMapping("/{Id}")
	public String putCourseUpdate(@RequestBody Course course, @PathVariable int Id) {
    return courseService.updateCourse(course, Id);
	}
	
//	Delete the course by course_Id
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@DeleteMapping("/{Id}")
	public String deleteCourseById(@PathVariable int Id) {
		return courseService.deleteCourseById(Id);
	}
	
//	delete All course
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	public String deleteAllCourses() {
		return courseService.deleteAllCourses();
	}
	
}
