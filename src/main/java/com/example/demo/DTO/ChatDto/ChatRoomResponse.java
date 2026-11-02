package com.example.demo.DTO.ChatDto;

import java.time.LocalDateTime;

import com.example.demo.model.Type.ChatRoomType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomResponse {

    private Integer id;

    private ChatRoomType roomType;

    private String roomName;

    private Integer courseSubjectId;

    private LocalDateTime createdAt;
}
