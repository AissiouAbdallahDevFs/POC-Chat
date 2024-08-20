// src/app/user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from '../models/user-dto.model' // Assure-toi d'avoir ce modèle

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/chat/users';

  constructor(private http: HttpClient) { }

  getConnectedUsers(): Observable<UserDTO[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<UserDTO[]>(this.apiUrl, { headers });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken'); // Adapté à ton stockage de token
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }
}
