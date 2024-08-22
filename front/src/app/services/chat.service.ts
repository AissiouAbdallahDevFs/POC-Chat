import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { PrivateMessage, PrivateChatRequestDTO } from '../models/private-message.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private stompClient: Stomp.Client | null = null;
  private privateMessagesSubject = new Subject<PrivateMessage>();
  private socketUrl = 'http://localhost:8080/gs-guide-websocket';

  constructor() {
    this.connect();
  }

  connect(): void {
    const socket = new SockJS(this.socketUrl);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);

      // Subscribe to the real-time message queue
      this.stompClient?.subscribe('/user/queue/private-messages', (message) => {
        const privateMessage = JSON.parse(message.body) as PrivateMessage;
        this.privateMessagesSubject.next(privateMessage);
      });
    });
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected');
      });
    }
  }

  sendPrivateMessage(senderId: number, recipientId: number, content: string): Observable<void> {
    const message = {
      senderId: senderId,
      recipientId: recipientId,
      content: content,
      timestamp: new Date().toISOString()
    };

    return new Observable<void>(observer => {
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send(`/app/send-private-message`, {}, JSON.stringify(message));
        observer.next();
        observer.complete();
      } else {
        observer.error('STOMP client not connected');
      }
    });
  }

  getPrivateMessages(senderId: number, recipientId: number): Observable<PrivateMessage[]> {
    const request: PrivateChatRequestDTO = {
      senderId: senderId,
      recipientId: recipientId
    };

    return new Observable<PrivateMessage[]>(observer => {
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send(`/app/get-private-messages`, {}, JSON.stringify(request));
        this.stompClient.subscribe('/user/queue/private-messages-history', (message: any) => {
          const privateMessages = JSON.parse(message.body) as PrivateMessage[];
          observer.next(privateMessages);
          observer.complete();
        });
      } else {
        observer.error('STOMP client not connected');
      }
    });
  }

  getRealTimePrivateMessages(): Observable<PrivateMessage> {
    return this.privateMessagesSubject.asObservable();
  }
}
