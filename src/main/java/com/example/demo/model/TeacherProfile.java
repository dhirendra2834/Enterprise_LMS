package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfile {

    @Id
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private User user;

    @Column(length = 150)
    private String qualification;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(length = 2000)
    private String bio;
}