package com.example.demo.DTO.ChatDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDirectChatRequest {

    @NotNull
    private Integer receiverId;
}
