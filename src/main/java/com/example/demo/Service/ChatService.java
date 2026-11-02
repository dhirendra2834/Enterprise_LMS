package com.example.demo.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DTO.ChatDto.ChatMessageResponse;
import com.example.demo.DTO.ChatDto.ChatRoomResponse;
import com.example.demo.DTO.ChatDto.CreateGroupChatRequest;
import com.example.demo.DTO.ChatDto.SendMessageRequest;
import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;
import com.example.demo.model.User;
import com.example.demo.model.Chat.ChatMessage;
import com.example.demo.model.Chat.ChatParticipant;
import com.example.demo.model.Chat.ChatRoom;
import com.example.demo.model.Chat.MessageReceipt;
import com.example.demo.model.Type.ChatRoomType;
import com.example.demo.model.Type.EnrollmentStatus;
import com.example.demo.model.Type.ReceiptStatus;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.ChatParticipantRepository;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.MessageReceiptRepository;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatService {

    private  ChatRoomRepository chatRoomRepository;
    private  ChatParticipantRepository participantRepository;
    private  ChatMessageRepository messageRepository;
    private  MessageReceiptRepository receiptRepository;

    private  UserRepository userRepository;
    private  CourseRepository courseSubjectRepository;
    private  EnrollmentRepository enrollmentRepository;


    // --------------------------------------------------
    // 1. CREATE DIRECT CHAT
    // --------------------------------------------------

    @Transactional
    public ChatRoomResponse createDirectChat(
            Integer receiverId,
            String email) {

        User sender = getUserByEmail(email);

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Receiver not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException(
                    "You cannot create a chat with yourself");
        }

        // Check whether a direct room already exists
        List<ChatParticipant> senderRooms =
                participantRepository.findByUserIdAndActiveTrue(
                        sender.getId());

        for (ChatParticipant participant : senderRooms) {

            ChatRoom room = participant.getChatRoom();

            if (room.getRoomType() != ChatRoomType.DIRECT
                    || !Boolean.TRUE.equals(room.getActive())) {
                continue;
            }

            boolean receiverExists =
                    participantRepository
                            .existsByChatRoomIdAndUserIdAndActiveTrue(
                                    room.getId(),
                                    receiver.getId());

            if (receiverExists) {
                return mapRoom(room);
            }
        }

        ChatRoom room = ChatRoom.builder()
                .roomType(ChatRoomType.DIRECT)
                .createdBy(sender)
                .active(true)
                .build();

        room = chatRoomRepository.save(room);

        addParticipant(room, sender);
        addParticipant(room, receiver);

        return mapRoom(room);
    }


    // --------------------------------------------------
    // 2. CREATE GROUP CHAT
    // --------------------------------------------------

    @Transactional
    public ChatRoomResponse createGroupChat(
            CreateGroupChatRequest request,
            String email) {

        User creator = getUserByEmail(email);

        List<Integer> participantIds =
                request.getParticipantIds().stream()
                        .distinct()
                        .toList();

        if (participantIds.size() < 1) {
            throw new IllegalArgumentException(
                    "At least one participant is required");
        }

        ChatRoom room = ChatRoom.builder()
                .roomType(ChatRoomType.GROUP)
                .roomName(request.getRoomName())
                .createdBy(creator)
                .active(true)
                .build();

        room = chatRoomRepository.save(room);

        addParticipant(room, creator);

        for (Integer participantId : participantIds) {

            if (participantId.equals(creator.getId())) {
                continue;
            }

            User participant = userRepository.findById(participantId)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Participant not found: "
                                            + participantId));

            addParticipant(room, participant);
        }

        return mapRoom(room);
    }


    // --------------------------------------------------
    // 3. CREATE COURSE CHAT
    // --------------------------------------------------

    @Transactional
    public ChatRoomResponse createCourseChat(
            Integer courseSubjectId,
            String email) {

        User loggedInUser = getUserByEmail(email);

        Course courseSubject =
                courseSubjectRepository.findById(courseSubjectId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Course subject not found"));

        User assignedTeacher = courseSubject.getTeacher();

        boolean isTeacher =
                assignedTeacher.getId().equals(
                        loggedInUser.getId());

        boolean isEnrolledStudent =
                enrollmentRepository
                        .existsByUserIdAndCourseIdAndStatus(
                                loggedInUser.getId(),
                                courseSubject.getId(),
                                EnrollmentStatus.ACTIVE);

        if (!isTeacher && !isEnrolledStudent) {
            throw new AccessDeniedException(
                    "You are not allowed to access this course chat");
        }

        // Reuse an existing course room
        ChatRoom room = chatRoomRepository
                .findByCourseIdAndActiveTrue(courseSubjectId)
                .orElse(null);

        if (room == null) {

            room = ChatRoom.builder()
                    .roomType(ChatRoomType.COURSE)
                    .roomName("Course Discussion")
                    .createdBy(assignedTeacher)
                    .course(courseSubject)
                    .active(true)
                    .build();

            room = chatRoomRepository.save(room);

            // Add assigned teacher
            addParticipant(room, assignedTeacher);
        }

        // Add all currently active enrolled students
        List<Enrollment> enrollments =
                enrollmentRepository.findByCourseIdAndStatus(
                        courseSubject.getId(),
                        EnrollmentStatus.ACTIVE);

        for (Enrollment enrollment : enrollments) {
            addParticipant(room, enrollment.getUser());
        }

        return mapRoom(room);
    }


    // --------------------------------------------------
    // 4. GET MY CHAT ROOMS
    // --------------------------------------------------

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getMyChatRooms(String email) {

        User user = getUserByEmail(email);

        return participantRepository
                .findByUserIdAndActiveTrue(user.getId())
                .stream()
                .filter(p -> Boolean.TRUE.equals(
                        p.getChatRoom().getActive()))
                .map(p -> mapRoom(p.getChatRoom()))
                .toList();
    }


    // --------------------------------------------------
    // 5. SEND MESSAGE
    // --------------------------------------------------

    @Transactional
    public ChatMessageResponse sendMessage(
            Integer roomId,
            SendMessageRequest request,
            String email) {

        User sender = getUserByEmail(email);

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Chat room not found"));

        validateRoomAccess(roomId, sender.getId());

        if (!Boolean.TRUE.equals(room.getActive())) {
            throw new IllegalStateException(
                    "Chat room is inactive");
        }

        String content = request.getContent().trim();

        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .sender(sender)
                .content(content)
                .messageType(request.getMessageType())
                .build();

        message = messageRepository.save(message);

        // Create one receipt for each recipient
        List<ChatParticipant> participants =
                participantRepository.findByChatRoomIdAndActiveTrue(
                        roomId);

        List<MessageReceipt> receipts = new ArrayList<>();

        for (ChatParticipant participant : participants) {

            User recipient = participant.getUser();

            if (recipient.getId().equals(sender.getId())) {
                continue;
            }

            MessageReceipt receipt = MessageReceipt.builder()
                    .message(message)
                    .recipient(recipient)
                    .status(ReceiptStatus.SENT)
                    .build();

            receipts.add(receipt);
        }

        receiptRepository.saveAll(receipts);

        return mapMessage(message);
    }


    // --------------------------------------------------
    // 6. GET CHAT MESSAGE HISTORY
    // --------------------------------------------------

    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessages(
            Integer roomId,
            int page,
            int size,
            String email) {

        User user = getUserByEmail(email);

        validateRoomAccess(roomId, user.getId());

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid pagination values");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "sentAt"));

        return messageRepository
                .findByChatRoomIdAndDeletedAtIsNullOrderBySentAtDesc(
                        roomId,
                        pageable)
                .map(this::mapMessage);
    }


    // --------------------------------------------------
    // 7. MARK MESSAGE AS DELIVERED
    // --------------------------------------------------

    @Transactional
    public void markDelivered(
            Long messageId,
            String email) {

        User recipient = getUserByEmail(email);

        MessageReceipt receipt =
                receiptRepository
                        .findByMessageIdAndRecipientId(
                                messageId,
                                recipient.getId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Message receipt not found"));

        if (receipt.getStatus() == ReceiptStatus.SENT) {

            receipt.setStatus(ReceiptStatus.DELIVERED);
            receipt.setDeliveredAt(LocalDateTime.now());

            receiptRepository.save(receipt);
        }
    }


    // --------------------------------------------------
    // 8. MARK MESSAGE AS READ
    // --------------------------------------------------

    @Transactional
    public void markRead(
            Long messageId,
            String email) {

        User recipient = getUserByEmail(email);

        MessageReceipt receipt =
                receiptRepository
                        .findByMessageIdAndRecipientId(
                                messageId,
                                recipient.getId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Message receipt not found"));

        if (receipt.getStatus() != ReceiptStatus.READ) {

            receipt.setStatus(ReceiptStatus.READ);
            receipt.setReadAt(LocalDateTime.now());

            if (receipt.getDeliveredAt() == null) {
                receipt.setDeliveredAt(LocalDateTime.now());
            }

            receiptRepository.save(receipt);
        }
    }


    // --------------------------------------------------
    // 9. MARK ALL MESSAGES IN ROOM AS READ
    // --------------------------------------------------

    @Transactional
    public void markRoomAsRead(
            Integer roomId,
            String email) {

        User user = getUserByEmail(email);

        validateRoomAccess(roomId, user.getId());

        List<ChatMessage> messages =
                messageRepository
                        .findByChatRoomIdAndDeletedAtIsNullOrderBySentAtDesc(
                                roomId,
                                Pageable.unpaged())
                        .getContent();

        for (ChatMessage message : messages) {

            receiptRepository
                    .findByMessageIdAndRecipientId(
                            message.getId(),
                            user.getId())
                    .ifPresent(receipt -> {

                        if (receipt.getStatus() != ReceiptStatus.READ) {

                            receipt.setStatus(ReceiptStatus.READ);
                            receipt.setReadAt(LocalDateTime.now());

                            if (receipt.getDeliveredAt() == null) {
                                receipt.setDeliveredAt(
                                        LocalDateTime.now());
                            }

                            receiptRepository.save(receipt);
                        }
                    });
        }

        ChatParticipant participant =
                participantRepository
                        .findByChatRoomIdAndUserIdAndActiveTrue(
                                roomId,
                                user.getId())
                        .orElseThrow();

        participant.setLastReadAt(LocalDateTime.now());

        participantRepository.save(participant);
    }


    // --------------------------------------------------
    // 10. GET UNREAD COUNT
    // --------------------------------------------------

    @Transactional(readOnly = true)
    public long getUnreadCount(String email) {

        User user = getUserByEmail(email);

        return receiptRepository.countByRecipientIdAndStatusNot(
                user.getId(),
                ReceiptStatus.READ);
    }


    // --------------------------------------------------
    // HELPER METHODS
    // --------------------------------------------------

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found"));
    }


    private void addParticipant(
            ChatRoom room,
            User user) {

        boolean exists =
                participantRepository
                        .existsByChatRoomIdAndUserIdAndActiveTrue(
                                room.getId(),
                                user.getId());

        if (exists) {
            return;
        }

        ChatParticipant participant =
                ChatParticipant.builder()
                        .chatRoom(room)
                        .user(user)
                        .active(true)
                        .build();

        participantRepository.save(participant);
    }


    private void validateRoomAccess(
            Integer roomId,
            Integer userId) {

        boolean allowed =
                participantRepository
                        .existsByChatRoomIdAndUserIdAndActiveTrue(
                                roomId,
                                userId);

        if (!allowed) {
            throw new AccessDeniedException(
                    "You are not a participant of this chat room");
        }
    }


    private ChatRoomResponse mapRoom(ChatRoom room) {

        return ChatRoomResponse.builder()
                .id(room.getId())
                .roomType(room.getRoomType())
                .roomName(room.getRoomName())
                .courseSubjectId(
                        room.getCourse() != null
                                ? room.getCourse().getId()
                                : null)
                .createdAt(room.getCreatedAt())
                .build();
    }


    private ChatMessageResponse mapMessage(
            ChatMessage message) {

        return ChatMessageResponse.builder()
                .id(message.getId())
                .roomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .sentAt(message.getSentAt())
                .build();
    }
}