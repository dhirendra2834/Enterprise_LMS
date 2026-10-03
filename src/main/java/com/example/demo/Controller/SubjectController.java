package com.example.demo.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.SubjectRequestDto;
import com.example.demo.DTO.SubjectResponseDto;
import com.example.demo.Service.SubjectService;
import com.example.demo.model.Subject;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@Controller
@RequestMapping("/subjects")

public class SubjectController {

	private SubjectService subjectService;

//	create Subject in the particular courseId
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	public ResponseEntity<String> createSubjectInside_Course(@RequestBody SubjectRequestDto subject, Authentication authentication) {
		String subjectCreatedOrNot = subjectService.createSubject(subject, authentication);
	return ResponseEntity.ok(subjectCreatedOrNot);
	}

//	get all Subject CourseId
	@GetMapping("/Course/{Id}")
	public ResponseEntity<List<SubjectResponseDto>> getAllSubjectsByCourseId(@PathVariable int Id) {
		List<SubjectResponseDto> subjects = subjectService.fetchAllSubjectByCourseId(Id);
		return ResponseEntity.ok(subjects);
	}

//	get Subject by subjectId
	@GetMapping("/{Id}")
	public ResponseEntity<SubjectResponseDto> getSubjectById(@PathVariable int Id) {
		SubjectResponseDto subjectResponseDto = subjectService.getSubjectById(Id);

		return ResponseEntity.ok(subjectResponseDto);
	}
	
//	get all Subjects;
	@GetMapping
	public ResponseEntity<List<SubjectResponseDto>> getAllSubjects() {
		List<SubjectResponseDto> subjectResponseDto = subjectService.getAllSubjects();

		return ResponseEntity.ok(subjectResponseDto);
	}


//	Update the Subject By SubjectId
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PutMapping("/{Id}")
	public ResponseEntity<String> updateSubject_subjectId(@RequestBody Subject subject, @PathVariable int Id) {
		String subjectUpdatedOrNot = subjectService.updateSubjectData_subjectId(subject, Id);
		return ResponseEntity.ok(subjectUpdatedOrNot);
	}

//	delete the subject by Id
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@DeleteMapping("{Id}")
	public ResponseEntity<String> deleteSubject_SubjectId(@PathVariable int Id) {
		String subjectDeleted = subjectService.deleteSubjectBysubjectId(Id);
		return ResponseEntity.ok(subjectDeleted);
	}
}
