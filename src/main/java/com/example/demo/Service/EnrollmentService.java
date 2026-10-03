package com.example.demo.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.EnrollmentRequestDto;
import com.example.demo.DTO.EnrollmentResponseDto;
import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;
import com.example.demo.model.User;
import com.example.demo.model.Type.EnrollmentStatus;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EnrollmentService {

	private EnrollmentRepository enrollmentRepository;
	
	private CourseRepository courseRepository;

//	create Enrollment
	public String createEnrollment(EnrollmentRequestDto enrollment, Authentication authentication) {
		User loggedInUser = (User) authentication.getPrincipal();

		boolean userExitswithCourse = enrollmentRepository.existsByUserIdAndCourseId(loggedInUser.getId(),
				enrollment.getCourseId());
		if(userExitswithCourse) {
			return "This user is already Enrolled in this course";
		}
		
		Optional<Course> courseExits= courseRepository.findById(enrollment.getCourseId());
		if(courseExits.isEmpty()) {
			return "This Course is not  exits in this platform";
		}
		// extracting Course Details form Course Id
		Course exitsCourseDetails= courseExits.get();
         // Updating the Field of Enrollment object
		 Enrollment enrollmentDetails = Enrollment.builder()
		            .user(loggedInUser)
		            .course(exitsCourseDetails)
		            .enrolledAt(LocalDateTime.now())
		            .status(EnrollmentStatus.PENDING)
		            .build();
		 
		 enrollmentRepository.save(enrollmentDetails);
		 
		 return "Enrollment created successfully";
	}

//	get Enrollment Details by Enrollment Id
	public EnrollmentResponseDto getEnrollmentDataById(Integer Id) {
       Optional<Enrollment> enrollmentExits= enrollmentRepository.findById(Id);
       
       if(enrollmentExits.isEmpty()) {
    	   throw new Error("This Enrollment is not exits ");
       }
       
       Enrollment enrollmentDetails= enrollmentExits.get();
       
       EnrollmentResponseDto enrollmentResponseDto= 
    		   EnrollmentResponseDto.builder()
    		   .id(enrollmentDetails.getId())
    		   .user(enrollmentDetails.getUser())
    		   .courseId(enrollmentDetails.getCourse().getId())
    		   .courseName(enrollmentDetails.getCourse().getName())
    		   .status(enrollmentDetails.getStatus())
    		   .enrolledAt(enrollmentDetails.getEnrolledAt())
    		   .progress(enrollmentDetails.getProgress())
    		   .build();
       
       return enrollmentResponseDto;
	}
	
//	get Self Enrollment Details 
	public EnrollmentResponseDto getSelfEnrollmentDetails(Authentication authentication) {
		User loggedInUser = (User) authentication.getPrincipal();
       
		 Optional<Enrollment> enrollmentExits= enrollmentRepository.findById(loggedInUser.getId());
	       
	       if(enrollmentExits.isEmpty()) {
	    	   throw new Error("This User is not enrolled in any Course");
	       }
       
       Enrollment enrollmentDetails= enrollmentExits.get();
       
       EnrollmentResponseDto enrollmentResponseDto= 
    		   EnrollmentResponseDto.builder()
    		   .id(enrollmentDetails.getId())
    		   .user(enrollmentDetails.getUser())
    		   .courseId(enrollmentDetails.getCourse().getId())
    		   .courseName(enrollmentDetails.getCourse().getName())
    		   .status(enrollmentDetails.getStatus())
    		   .enrolledAt(enrollmentDetails.getEnrolledAt())
    		   .progress(enrollmentDetails.getProgress())
    		   .build();
       
       return enrollmentResponseDto;
	}
	
//	Covert the Enrollment to enrollmentresponse DTO 
	public EnrollmentResponseDto convertToEnrollmentResponseDto(Enrollment enrollment) {

	    return EnrollmentResponseDto.builder()
	            .id(enrollment.getId())
	            .user(enrollment.getUser())
	            .courseId(enrollment.getCourse().getId())
	            .courseName(enrollment.getCourse().getName())
	            .enrolledAt(enrollment.getEnrolledAt())
	            .status(enrollment.getStatus())
	            .progress(enrollment.getProgress())
	            .build();
	}

//	get All Enrollment data by Particular Course Id
	public List<EnrollmentResponseDto> getAllEnrollmentDataByCourseId(Integer id) {

	    Optional<Course> courseExists = courseRepository.findById(id);

	    if (courseExists.isEmpty()) {
	        throw new RuntimeException("This course does not exist");
	    }

	    List<Enrollment> enrollmentsData =
	            enrollmentRepository.findByCourseId(id);

	    return enrollmentsData.stream()
	            .map(this::convertToEnrollmentResponseDto)
	            .toList();
	}

//	 Update the Enrollment Details By Enrollment Id
	public String updateEnrollmentDetailsById(Integer Id) {
		   Optional<Enrollment> enrollmentExits= enrollmentRepository.findById(Id);
	       
	       if(enrollmentExits.isEmpty()) {
	    	   throw new Error("This Enrollment is not exits ");
	       }
	       
	       Enrollment enrollmentDetails= enrollmentExits.get();
	       
            // Updating  the enrollment details
	       enrollmentDetails.setUser(enrollmentDetails.getUser());
	    	   enrollmentDetails.setCourse(enrollmentDetails.getCourse());
	    	   enrollmentDetails.setStatus(enrollmentDetails.getStatus());
	    		 
		return "Enrollment details is updated ";
	}

//	Delete the Enrollment Details By Enrollment Id
	public String deleteEnrollmentDetailsById(Integer Id) {
		  Optional<Enrollment> enrollmentExits= enrollmentRepository.findById(Id);
	       
	       if(enrollmentExits.isEmpty()) {
	    	   throw new Error("This Enrollment is not exits ");
	       }
	       
	       enrollmentRepository.deleteById(Id);
	       return "This Enrollment is deleted Sucessfully";
	}

}
