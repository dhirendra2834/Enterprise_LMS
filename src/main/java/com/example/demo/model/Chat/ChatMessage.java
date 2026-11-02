package com.example.demo.model.Chat;

import java.time.LocalDateTime;

import com.example.demo.model.User;
import com.example.demo.model.Type.MessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "chat_messages",
    indexes = {
        @Index(
            name = "idx_chat_room_sent_at",
            columnList = "chat_room_id, sent_at"
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, length = 5000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType messageType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt;

    private LocalDateTime editedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    public void onCreate() {
        sentAt = LocalDateTime.now();

        if (messageType == null) {
            messageType = MessageType.TEXT;
        }
    }
}