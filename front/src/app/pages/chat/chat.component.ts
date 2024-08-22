import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ChatService } from '../../services/chat.service';
import { Subscription } from 'rxjs';
import { UserOnlineDTO } from '../../models/user-dto.model';
import { PrivateMessage } from '../../models/private-message.model';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {
  users: UserOnlineDTO[] = [];
  privateMessages: PrivateMessage[] = [];
  selectedUser: UserOnlineDTO | null = null;
  privateMessage: string = '';
  showChatPopup: boolean = false;
  private messagesSubscription: Subscription = new Subscription();
  private usersOnlineSubscription: Subscription = new Subscription();
  private currentUserSubscription: Subscription = new Subscription();
  private realTimeMessagesSubscription: Subscription = new Subscription();
  currentUserId: number | null = null;

  constructor(
    private authService: AuthService,
    private chatService: ChatService
  ) {}

  ngOnInit(): void {
    this.initializeChat();
  }

  ngOnDestroy(): void {
    this.chatService.disconnect();
    this.messagesSubscription.unsubscribe();
    this.usersOnlineSubscription.unsubscribe();
    this.currentUserSubscription.unsubscribe();
    this.realTimeMessagesSubscription.unsubscribe();
  }

  initializeChat(): void {
    this.authService.getCurrentUser().subscribe(user => {
      if (user) {
        this.currentUserId = user.id;
        this.loadUsersOnline();
        this.subscribeToRealTimeMessages();
      }
    });
  }

  loadUsersOnline(): void {
    this.usersOnlineSubscription = this.authService.getUsersOnline().subscribe(users => {
      if (this.currentUserId) {
        this.users = users.filter(user => user.id !== this.currentUserId);
      } else {
        this.users = users;
      }
    });
  }

  loadPrivateMessages(): void {
    if (this.selectedUser && this.currentUserId) {
      this.messagesSubscription = this.chatService.getPrivateMessages(this.currentUserId, this.selectedUser.id).subscribe(messages => {
        this.privateMessages = messages;
        this.privateMessages.sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());
      });
    }
  }

  sendPrivateMessage(): void {
    if (this.privateMessage.trim() && this.selectedUser && this.currentUserId) {
      this.chatService.sendPrivateMessage(this.currentUserId, this.selectedUser.id, this.privateMessage).subscribe(() => {
        this.privateMessage = '';
      });
    }
  }

  selectUser(user: UserOnlineDTO): void {
    this.selectedUser = user;
    this.showChatPopup = true;
    this.loadPrivateMessages();
  }

  closeChatPopup(): void {
    this.showChatPopup = false;
    this.selectedUser = null;
    this.privateMessages = [];
  }

  private subscribeToRealTimeMessages(): void {
    this.realTimeMessagesSubscription = this.chatService.getRealTimePrivateMessages().subscribe(message => {
      if (this.selectedUser && this.currentUserId) {
        if ((message.senderId === this.currentUserId || message.recipientId === this.currentUserId) &&
            (message.senderId === this.selectedUser.id || message.recipientId === this.selectedUser.id)) {
          this.privateMessages.push(message);
          this.privateMessages.sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());
        }
      }
    });
  }
}
