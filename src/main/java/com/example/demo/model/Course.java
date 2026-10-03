package com.example.demo.model;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.demo.model.Subject;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	 @Column(nullable = false)
	private String name;
	private String description;

//	Many to many their different table will create "course_subject" so inside 1 course can store multiple subjects 
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "course_subject", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private List<Subject> subjects;
    
	 @Column(nullable = false)
	private BigDecimal fees;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_By")
	private User createdBy;

	private LocalDateTime createdAt;
	
	 @PrePersist
	  public void prePersist() {
	        createdAt = LocalDateTime.now();
	   }

}
