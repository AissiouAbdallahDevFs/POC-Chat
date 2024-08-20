import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { io, Socket } from 'socket.io-client';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service'; // Assure-toi que ce service est importé

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private socket: Socket = {} as Socket;
  private userId: string = '';

  constructor(private http: HttpClient, private authService: AuthService) {
    this.getCurrentUserId().subscribe(id => {
      this.userId = id;
      this.initializeSocketConnection();
    });
  }

  private initializeSocketConnection() {
    const token = this.authService.getToken(); 
    this.socket = io('http://localhost:8086', {
      query: {
        userId: this.userId,
        token: token 
      },
      transports: ['websocket'], 
    });
  }

  getCurrentUserId(): Observable<string> {
    return this.http.get<string>('http://localhost:8080/api/auth/current-user-id');
  }

  getOnlineUsers(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/usersOnline');
  }

  sendMessage(message: any) {
    this.socket.emit('privateMessage', message);
  }

  onNewMessage(): Observable<any> {
    return new Observable(observer => {
      this.socket.on('privateMessage', (message) => {
        observer.next(message);
      });
    });
  }
}
