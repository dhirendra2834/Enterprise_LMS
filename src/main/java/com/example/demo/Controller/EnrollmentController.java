package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.EnrollmentRequestDto;
import com.example.demo.DTO.EnrollmentResponseDto;
import com.example.demo.Service.EnrollmentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/enrollment")

public class EnrollmentController {

	private EnrollmentService enrollmentService;
	
//	create Enrollment 
	public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentRequestDto enrollment, Authentication authentication){
		return ResponseEntity.ok(enrollmentService.createEnrollment(enrollment, authentication));
	}
//	get self Enrollment Details
	@GetMapping("/me")
	public ResponseEntity<EnrollmentResponseDto> getSelfEnrollmentDetails(Authentication authentication) {
		EnrollmentResponseDto enrollmentResponseDto = enrollmentService.getSelfEnrollmentDetails(authentication);
		return ResponseEntity.ok(enrollmentResponseDto);
	}

// get Enrollment Details By enrollment Id	
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@GetMapping("{Id}")
	public ResponseEntity<EnrollmentResponseDto> getEnrollmentDetailsById(@PathVariable Integer Id) {
		EnrollmentResponseDto enrollmentResponseDto = enrollmentService.getEnrollmentDataById(Id);
		return ResponseEntity.ok(enrollmentResponseDto);
	}

//	get All Enrollment Details  by Course Id
	@GetMapping("/course/{Id}")
	public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollmentDetailsByCourseId(@PathVariable Integer Id) {
		List<EnrollmentResponseDto> enrollmentsDetails = enrollmentService.getAllEnrollmentDataByCourseId(Id);
		return ResponseEntity.ok(enrollmentsDetails);
	}
	
	@PutMapping("{Id}")
	public ResponseEntity<String> updateEnrollmentDetailsById(@PathVariable Integer Id) {
		String enrollmentUpdated = enrollmentService.updateEnrollmentDetailsById(Id);
		return ResponseEntity.ok(enrollmentUpdated);
	}
	
	@DeleteMapping("{Id}")
	public ResponseEntity<String> deleteEnrollmentDetailsById(@PathVariable Integer Id) {
		String enrollmentDeleted = enrollmentService.deleteEnrollmentDetailsById(Id);
		return ResponseEntity.ok(enrollmentDeleted);
	}

}
