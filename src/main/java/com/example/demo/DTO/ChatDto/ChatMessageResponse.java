package com.example.demo.DTO.ChatDto;

import java.time.LocalDateTime;

import com.example.demo.model.Type.MessageType;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ChatMessageResponse {

    private Long id;

    private Integer roomId;

    private Integer senderId;

    private String senderName;

    private String content;

    private MessageType messageType;

    private LocalDateTime sentAt;
}