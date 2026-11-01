package com.example.demo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Chat.ChatParticipant;

public interface ChatParticipantRepository
        extends JpaRepository<ChatParticipant, Integer> {

    boolean existsByChatRoomIdAndUserIdAndActiveTrue(
        Integer roomId,
        Integer userId
    );

    Optional<ChatParticipant>
    findByChatRoomIdAndUserIdAndActiveTrue(
        Integer roomId,
        Integer userId
    );

    List<ChatParticipant> findByChatRoomIdAndActiveTrue(
        Integer roomId
    );

    List<ChatParticipant> findByUserIdAndActiveTrue(
        Integer userId
    );

    List<ChatParticipant> findByChatRoomIdAndUserIdInAndActiveTrue(
        Integer roomId,
        List<Integer> userIds
    );
}