package com.example.demo.DTO.ChatDto;


import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class CreateGroupChatRequest {

    @NotBlank
    @Size(max = 150)
    private String roomName;

    @NotEmpty
    private List<Integer> participantIds;
}