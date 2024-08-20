package com.POC.Chat.dto;

public class PrivateChatRequestDTO {

    private Long userId;
    private Long recipientId;


    public PrivateChatRequestDTO() {}

    public PrivateChatRequestDTO(Long userId, Long recipientId) {
        this.userId = userId;
        this.recipientId = recipientId;
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
    
}
