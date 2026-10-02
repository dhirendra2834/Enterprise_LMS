package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Subject;

public interface SubjectRepository  extends JpaRepository<Subject, Integer>{

   
	  @Query("""
		        SELECT DISTINCT s
		        FROM Subject s
		        JOIN s.courses c
		        WHERE c.id = :courseId
		    """)
		    List<Subject> findAllSubjectsByCourseId(@Param("courseId") Integer courseId);
}
