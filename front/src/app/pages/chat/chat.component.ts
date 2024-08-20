import { Component, OnInit } from '@angular/core';
import { ChatService } from 'src/app/services/chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  message = '';
  messages: any[] = [];
  recipientId = '';
  onlineUsers: any[] = [];
  selectedUserName = '';

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.loadOnlineUsers();

    this.chatService.onNewMessage().subscribe((message: any) => {
      this.messages.push(message);
    });
  }

  loadOnlineUsers() {
    this.chatService.getOnlineUsers().subscribe((users: any[]) => {
      this.onlineUsers = users;
    });
  }

  sendMessage() {
    if (this.message.trim() && this.recipientId) {
      const messagePayload = {
        senderId: this.chatService.getCurrentUserId(), // Remplace par l'ID actuel de l'utilisateur
        recipientId: this.recipientId,
        content: this.message
      };
      this.chatService.sendMessage(messagePayload);
      this.message = '';
    }
  }

  selectUser(id: string) {
    this.recipientId = id;
    this.selectedUserName = this.onlineUsers.find(user => user.id === id)?.name || '';
  }

  closeChat() {
    this.recipientId = '';
    this.selectedUserName = '';
  }
}
