package com.POC.Chat.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.POC.Chat.model.PrivateMessage;

@Data
public class PrivateMessageDTO {
    private Long senderId;  
    private Long recipientId;  
    private String content;
    private LocalDateTime timestamp;

    // Constructeur par défaut
    public PrivateMessageDTO() {}

    // Constructeur complet
    public PrivateMessageDTO(Long senderId, Long recipientId, String content, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Constructeur basé sur l'entité PrivateMessage
    public PrivateMessageDTO(PrivateMessage message) {
        this.senderId = message.getSender().getId();
        this.recipientId = message.getRecipient().getId();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp().toLocalDateTime();
    }

}
