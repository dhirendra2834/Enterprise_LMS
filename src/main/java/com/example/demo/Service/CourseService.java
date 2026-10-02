package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.CourseDetailResponseDto;
import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CourseService {
	
   private CourseRepository courseRepository;
   
//   create Course
   public String createCourse(Course course) {
		System.out.println("course service running ");

	   Course courseExits= courseRepository.findByName(course.getName());
	   if(courseExits!=null) {
		 return "This Course Already exits"; 
	   }
	   courseRepository.save(course);
		System.out.println("course save successfully ");
	   return "Course "+course.getName()+" created Successfully";
   }

//  get Course Data By course_id
   public CourseDetailResponseDto getCourseById(int id) {
	  Optional<Course> courseData= courseRepository.findById(id);
	  
	  if(courseData.isEmpty()) {
	        throw new RuntimeException("This Course is not exits");
	  }
	 CourseDetailResponseDto courseDetailResponseDto= 
			 CourseDetailResponseDto.builder()
			 .name(courseData.get().getName())
			 .id(courseData.get().getId())
			 .description(courseData.get().getDescription())
			 .subjects(courseData.get().getSubjects())
			 .fees(courseData.get().getFees())
			 .build();
	 	
	return courseDetailResponseDto;
   }
// get All course 
   public List<CourseDetailResponseDto> getAllCourses() {

	    List<Course> courses = courseRepository.findAll();

	    return courses.stream()
	            .map(course -> CourseDetailResponseDto.builder()
	                    .id(course.getId())
	                    .name(course.getName())
	                    .description(course.getDescription())
	                    .subjects(course.getSubjects())
	                    .fees(course.getFees())
	                    .build())
	            .toList();
	}
//  update the course by Id 
   public String updateCourse(Course course, int id) {

	    Optional<Course> courseExists = courseRepository.findById(id);

	    if (courseExists.isEmpty()) {
	        return "This course does not exist";
	    }

	    Course existingCourse = courseExists.get();

	    existingCourse.setName(course.getName());
	    existingCourse.setDescription(course.getDescription());
	    existingCourse.setSubjects(course.getSubjects());
	    existingCourse.setFees(course.getFees());

	    courseRepository.save(existingCourse);

	    return "Course " + existingCourse.getName() + " updated successfully";
	}

//   deleted the course Successfully
   public String deleteCourseById(int id) {
	   Optional<Course> courseExists = courseRepository.findById(id);

	    if (courseExists.isEmpty()) {
	        return "This course does not exist";
	    }
       
	    courseRepository.deleteById(id);

	    return "Course deleted successfully";
	
   }
// Delete All courses
   public String deleteAllCourses() {
    courseRepository .deleteAll();
	return "All courses Deleted";
   }
	
}
