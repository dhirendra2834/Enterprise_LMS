package com.example.demo.DTO.ChatDto;

import com.example.demo.model.Type.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendMessageRequest {

    @NotBlank
    @Size(max = 5000)
    private String content;

    @NotNull
    private MessageType messageType;
}