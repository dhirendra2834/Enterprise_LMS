package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Chat.ChatMessage;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage>
    findByChatRoomIdAndDeletedAtIsNullOrderBySentAtDesc(
        Integer roomId,
        Pageable pageable
    );
}