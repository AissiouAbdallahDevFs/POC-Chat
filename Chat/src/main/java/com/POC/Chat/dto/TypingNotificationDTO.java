package com.POC.Chat.dto;

import lombok.Data;

@Data
public class TypingNotificationDTO {
    private Long senderId;
    private Long recipientId;
    
    // getters et setters
}
