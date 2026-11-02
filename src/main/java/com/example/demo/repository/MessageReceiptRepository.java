package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Chat.MessageReceipt;
import com.example.demo.model.Type.ReceiptStatus;

public interface MessageReceiptRepository
        extends JpaRepository<MessageReceipt, Long> {

    Optional<MessageReceipt> findByMessageIdAndRecipientId(
        Long messageId,
        Integer recipientId
    );

    List<MessageReceipt> findByMessageId(Long messageId);

    List<MessageReceipt> findByRecipientIdAndStatusNot(
        Integer recipientId,
        ReceiptStatus status
    );

    long countByRecipientIdAndStatusNot(
        Integer recipientId,
        ReceiptStatus status
    );
}