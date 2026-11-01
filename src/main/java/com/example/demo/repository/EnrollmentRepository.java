package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Type.EnrollmentStatus;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer>{
	
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);
    
    List<Enrollment> findByCourseId(Integer courseId);

    boolean existsByUserIdAndCourseIdAndStatus(
    	    Integer userId,
    	    Integer courseId,
    	    EnrollmentStatus status
    	);

    	List<Enrollment> findByCourseIdAndStatus(
    	    Integer courseId,
    	    EnrollmentStatus status
    	);

}
