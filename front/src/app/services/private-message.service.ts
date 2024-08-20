import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Socket } from 'ngx-socket-io';
import { PrivateMessage } from '../models/private-message.model';

@Injectable({
  providedIn: 'root'
})
export class PrivateChatService {
  private apiUrl = '/api/private-messages';  // URL pour les requêtes HTTP
  private apiUrlSend = '/api/private-messages/send';  // URL pour les requêtes HTTP

  constructor(private http: HttpClient, private socket: Socket) {}

  getMessages(recipientId: number): Observable<PrivateMessage[]> {
    return this.http.get<PrivateMessage[]>(`${this.apiUrl}?recipientId=${recipientId}`);
  }

  sendMessage(message: PrivateMessage): Observable<any> {
    // Utiliser le Socket.IO pour envoyer le message en temps réel
    this.socket.emit('send-message', message);
    return this.http.post(this.apiUrlSend, message);
  }

  // Écouter les messages reçus en temps réel
  getMessageUpdates(): Observable<PrivateMessage> {
    return this.socket.fromEvent<PrivateMessage>('new-message');
  }
}
