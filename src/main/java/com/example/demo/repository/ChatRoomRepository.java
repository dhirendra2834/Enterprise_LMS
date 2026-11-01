package com.example.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Chat.ChatRoom;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Integer> {

	  Optional<ChatRoom> findByCourseIdAndActiveTrue(Integer courseId);

	    boolean existsByCourseIdAndActiveTrue(Integer courseId);
}