package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {


	   @Query("""
		        SELECT c
		        FROM Chapter c
		        WHERE c.name = :name
		        AND c.subject.id = :subjectId
		    """)
	 Chapter findByName(@Param("name") String name, @Param("subjectId") Integer subjectId);
	   
	   List<Chapter> findAllBySubject_Id(Integer subjectId);

	    Chapter findByNameAndSubject_Id(String name, Integer subjectId);

}
