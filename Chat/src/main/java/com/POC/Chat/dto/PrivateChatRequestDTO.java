package com.POC.Chat.dto;

public class PrivateChatRequestDTO {

    private Long userId;
    private Long recipientId;
    private Long senderId;


    public PrivateChatRequestDTO() {}

    public PrivateChatRequestDTO(Long userId, Long recipientId, Long senderId) {
        this.userId = userId;
        this.recipientId = recipientId;
        this.senderId = senderId;
    }



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
    
    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

 
}
