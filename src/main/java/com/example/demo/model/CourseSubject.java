//package com.example.demo.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(
//    name = "course_subject",
//    uniqueConstraints = {
//        @UniqueConstraint(
//            name = "uk_course_subject",
//            columnNames = {"course_id", "subject_id"}
//        )
//    }
//)
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class CourseSubject {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "course_id", nullable = false)
//    private Course course;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "subject_id", nullable = false)
//    private Subject subject;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "teacher_id", nullable = false)
//    private User teacher;
//}