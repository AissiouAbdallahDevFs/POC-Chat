// src/app/models/private-message.model.ts
export interface PrivateMessage {
  senderId: number;
  recipientId: number;
  content: string;
  timestamp: string;
}

export interface TypingNotificationDTO {
  senderId: number;
  recipientId: number;
}
// private-chat-request.dto.ts
export interface PrivateChatRequestDTO {
  senderId: number;
  recipientId: number;
}
