package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
//GET    /api/courses/{courseId}/subjects
//
//GET    /api/subjects/{id}
//
//POST   /api/courses/{courseId}/subjects
//
//PUT    /api/subjects/{id}
//
//DELETE /api/subjects/{id}
public class SubjectController {

	private SubjectService subjectService;

//	create Subject in the particular courseId
	@PostMapping("/{Id}/subject")
	public ResponseEntity<String> createSubjectInside_Course(@RequestBody Subject subject, @PathVariable int Id){
		String subjectCreatedOrNot= subjectService.createSubjectInside_ByCourseId(subject, Id);
		ResponseEntity.ok(subjectCreatedOrNot);
	}

//	get all Subject CourseId
	@GetMapping("/{Id}")
	public ResponseEntity<List<SubjectResponseDto>> getAllSubjectsByCourseId(@PathVariable int Id) {
		List<SubjectResponseDto> subjects = subjectService.fetchAllSubjectByCourseId(Id);
		ResponseEntity.ok(subjects);
	}

//	get Subject by subjectId
	@GetMapping("/{Id}")
	public ResponseEntity<SubjectResponseDto> getSubjectById(@PathVariable int Id) {
		SubjectResponseDto subjectResponseDto = subjectService.getSubjectById(Id);

		return ResponseEntity.ok(subjectResponseDto);
	}
	
//	Update the Subject By SubjectId
	@PutMapping("/{Id}")
	public ResponseEntity<String> updateSubject_subjectId(@RequestBody Subject subject, @PathVariable int Id){
		String subjectUpdatedOrNot= subjectService.updateSubjectData_subjectId(subject, Id);
		return ResponseEntity.ok(subjectUpdatedOrNot);
	}
	
//	delete the subject by Id
	@DeleteMapping
	public ResponseEntity<String> deleteSubject_SubjectId(@PathVariable int Id){
		String subjectDeleted= subjectService.deleteSubjectBysubjectId(Id);
		return ResponseEntity.ok(subjectDeleted);
	}
}
