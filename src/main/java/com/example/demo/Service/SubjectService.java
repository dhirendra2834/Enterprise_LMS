package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;


import org.springframework.stereotype.Service;

import com.example.demo.DTO.SubjectRequestDto;
import com.example.demo.DTO.SubjectResponseDto;
import com.example.demo.model.Course;
import com.example.demo.model.Subject;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.SubjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SubjectService {

	
	private SubjectRepository subjectRepository;
	
	private CourseRepository courseRepository;
	
// create subject inside the particular Course;
	
	public String createSubject(SubjectRequestDto subject,   Authentication authentication) {

	    // 1. Get Course IDs from DTO
	    List<Integer> courseIds = subject.getCourseIds();

	    // 2. Find courses from database
	    List<Course> courses =
	            courseRepository.findAllById(courseIds);

	    // 3. Check whether all courses exist
	    if (courses.size() != courseIds.size()) {
	        throw new RuntimeException("One or more courses do not exist");
	    }

	    // 4. Get currently logged-in user
	    User loggedInUser = (User) authentication.getPrincipal();

	    // 5. Create Subject
	    Subject subjectData = Subject.builder()
	            .name(subject.getName())
	            .description(subject.getDescription())
	            .courses(courses)
	            .createdBy(loggedInUser)
	            .build();

	    // 6. Save Subject
	    subjectRepository.save(subjectData);

	    // 7. Course is the owning side of ManyToMany
	    for (Course course : courses) {

	        if (course.getSubjects() == null) {
	            course.setSubjects(new ArrayList<>());
	        }

	        course.getSubjects().add(subjectData);
	    }

	    // 8. Save relationship in course_subject table
	    courseRepository.saveAll(courses);

	    return "Subject " + subjectData.getName()
	            + " created successfully";
	}
//	update the SubjectData by subjectId
	public String updateSubjectData_subjectId(Subject subject, int id) {
		
	Optional<Subject> subjectExits= subjectRepository.findById(id);
		
		if(subjectExits.isEmpty()) {
	        throw new RuntimeException("This subject is not exits");
		};
         
		Subject existingSubjectData= subjectExits.get();
		existingSubjectData.setName(subject.getName());
		existingSubjectData.setDescription(subject.getDescription());
		existingSubjectData.setChapters(subject.getChapters());
		existingSubjectData.setCourses(subject.getCourses());
		
		subjectRepository.save(existingSubjectData);
		
		return "latest subject data is updated";
	}
	
//	get subjectData By subjectId
	
	public SubjectResponseDto getSubjectById(int id) {
		Optional<Subject> subjectExits= subjectRepository.findById(id);
		
		if(subjectExits.isEmpty()) {
	        throw new RuntimeException("This subject is not exits");
		};
		
		SubjectResponseDto subjectResponseDto= 
				SubjectResponseDto.builder()
				.name(subjectExits.get().getName())
				.description(subjectExits.get().getDescription())
				.chapters(subjectExits.get().getChapters())
				.id(subjectExits.get().getId())
				.courses(subjectExits.get().getCourses())
				.build();
		
		return subjectResponseDto;
	}
//	for response prepare 
	private SubjectResponseDto convertToSubjectResponseDto(Subject subject) {

	    SubjectResponseDto subjectResponseDto= SubjectResponseDto.builder()
	            .id(subject.getId())
	            .name(subject.getName())
	            .description(subject.getDescription())
	            .courses(subject.getCourses())
	            .chapters(subject.getChapters())
	            .build();
	    
	    return subjectResponseDto;
	}

//	get all Subject data By CourseId
	public List<SubjectResponseDto> fetchAllSubjectByCourseId(int id) {
		
		Optional<Course> courseExits= courseRepository.findById(id); 
		if(courseExits.isEmpty()) {
	        throw new RuntimeException("This subject is not exits");
		}
		
		List<Subject> subjects= subjectRepository.findAllSubjectsByCourseId(id);
	
		return subjects.stream()
	            .map(this::convertToSubjectResponseDto)
	            .toList();
	}
	
//	get all Subjects
	public List<SubjectResponseDto> getAllSubjects() {
		
	
		List<Subject> subjects= subjectRepository.findAll();
	
		return subjects.stream()
	            .map(this::convertToSubjectResponseDto)
	            .toList();
	}
	
//	delete the subject by SubjectId
	public String deleteSubjectBysubjectId(int id) {
		Optional<Subject> subjectExits= subjectRepository.findById(id);
		
		if(subjectExits.isEmpty()) {
			return "This Subject is not exits"; 
		}
		
		subjectRepository.deleteById(id);
		return "subject "+subjectExits.get().getName()+" is deleted sucessfully";
	}


}
