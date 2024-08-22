package com.POC.Chat.controller;

import com.POC.Chat.dto.PrivateChatRequestDTO;
import com.POC.Chat.dto.PrivateMessageDTO;
import com.POC.Chat.dto.TypingNotificationDTO;
import com.POC.Chat.model.PrivateMessage;
import com.POC.Chat.service.PrivateMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    private final PrivateMessageService privateMessageService;

    public WebSocketController(PrivateMessageService privateMessageService) {
        this.privateMessageService = privateMessageService;
    }

    /**
     * Handles sending a private message. 
     * The message is saved to the database and then broadcast to the recipient.
     */
    @MessageMapping("/send-private-message")
    @SendToUser("/queue/private-messages")
    public PrivateMessageDTO handlePrivateMessage(PrivateMessageDTO messageDTO) {
        // Save the message
        PrivateMessage savedMessage = privateMessageService.sendMessage(
                messageDTO.getSenderId(),
                messageDTO.getRecipientId(),
                messageDTO.getContent()
        );
        // Return the saved message to be broadcasted
        return new PrivateMessageDTO(savedMessage);
    }

    /**
     * Handles retrieving message history between two users.
     */
    @MessageMapping("/get-private-messages")
    @SendToUser("/queue/private-messages-history")
    public List<PrivateMessageDTO> getPrivateMessages(PrivateChatRequestDTO request) {
        Long senderId = request.getSenderId();
        Long recipientId = request.getRecipientId();

        List<PrivateMessage> messages = privateMessageService.getMessagesBetweenUsers(senderId, recipientId);
        // Convert and return the list of messages
        return messages.stream()
                .map(PrivateMessageDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Handles sending typing notifications.
     */
    @MessageMapping("/typing-notification")
    @SendToUser("/queue/typing-notifications")
    public TypingNotificationDTO handleTypingNotification(TypingNotificationDTO notification) {
        // Broadcast the typing notification to the intended recipient
        return notification;
    }

    /**
     * Handles real-time message updates.
     */
    @MessageMapping("/real-time-message")
    @SendToUser("/queue/real-time-messages")
    public PrivateMessageDTO handleRealTimeMessage(PrivateMessageDTO messageDTO) {
        // Broadcast the real-time message
        return messageDTO;
    }
}
