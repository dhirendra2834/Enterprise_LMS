package com.example.demo.DTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubjectRequestDto {

    private String name;
    private String description;
    private List<Integer> courseIds;
}
