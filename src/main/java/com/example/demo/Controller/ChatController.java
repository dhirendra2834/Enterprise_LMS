package com.example.demo.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ChatDto.ChatMessageResponse;
import com.example.demo.DTO.ChatDto.ChatRoomResponse;
import com.example.demo.DTO.ChatDto.CreateDirectChatRequest;
import com.example.demo.DTO.ChatDto.CreateGroupChatRequest;
import com.example.demo.DTO.ChatDto.SendMessageRequest;
import com.example.demo.Service.ChatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    // -----------------------------------------
    // 1. CREATE DIRECT CHAT
    // -----------------------------------------

    @PostMapping("/direct")
    public ResponseEntity<ChatRoomResponse> createDirectChat(
            @Valid @RequestBody CreateDirectChatRequest request,
            Authentication authentication) {

        ChatRoomResponse response =
                chatService.createDirectChat(
                        request.getReceiverId(),
                        authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    // -----------------------------------------
    // 2. CREATE GROUP CHAT
    // -----------------------------------------

    @PostMapping("/group")
    public ResponseEntity<ChatRoomResponse> createGroupChat(
            @Valid @RequestBody CreateGroupChatRequest request,
            Authentication authentication) {

        ChatRoomResponse response =
                chatService.createGroupChat(
                        request,
                        authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    // -----------------------------------------
    // 3. CREATE / GET COURSE CHAT
    // -----------------------------------------

    @PostMapping("/course/{courseSubjectId}")
    public ResponseEntity<ChatRoomResponse> createCourseChat(
            @PathVariable Integer courseSubjectId,
            Authentication authentication) {

        ChatRoomResponse response =
                chatService.createCourseChat(
                        courseSubjectId,
                        authentication.getName());

        return ResponseEntity.ok(response);
    }


    // -----------------------------------------
    // 4. GET MY CHAT ROOMS
    // -----------------------------------------

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getMyChatRooms(
            Authentication authentication) {

        return ResponseEntity.ok(
                chatService.getMyChatRooms(
                        authentication.getName()));
    }


    // -----------------------------------------
    // 5. SEND MESSAGE
    // -----------------------------------------

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @PathVariable Integer roomId,
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication) {

        ChatMessageResponse response =
                chatService.sendMessage(
                        roomId,
                        request,
                        authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    // -----------------------------------------
    // 6. GET MESSAGE HISTORY
    // -----------------------------------------

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(
            @PathVariable Integer roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            Authentication authentication) {

        return ResponseEntity.ok(
                chatService.getMessages(
                        roomId,
                        page,
                        size,
                        authentication.getName()));
    }


    // -----------------------------------------
    // 7. MARK MESSAGE DELIVERED
    // -----------------------------------------

    @PutMapping("/messages/{messageId}/delivered")
    public ResponseEntity<Void> markDelivered(
            @PathVariable Long messageId,
            Authentication authentication) {

        chatService.markDelivered(
                messageId,
                authentication.getName());

        return ResponseEntity.noContent().build();
    }


    // -----------------------------------------
    // 8. MARK MESSAGE READ
    // -----------------------------------------

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long messageId,
            Authentication authentication) {

        chatService.markRead(
                messageId,
                authentication.getName());

        return ResponseEntity.noContent().build();
    }


    // -----------------------------------------
    // 9. MARK ENTIRE ROOM AS READ
    // -----------------------------------------

    @PutMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markRoomAsRead(
            @PathVariable Integer roomId,
            Authentication authentication) {

        chatService.markRoomAsRead(
                roomId,
                authentication.getName());

        return ResponseEntity.noContent().build();
    }


    // -----------------------------------------
    // 10. UNREAD MESSAGE COUNT
    // -----------------------------------------

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            Authentication authentication) {

        return ResponseEntity.ok(
                chatService.getUnreadCount(
                        authentication.getName()));
    }
}