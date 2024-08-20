import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
import { UserDTO } from '../models/user-dto.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth'; // Assurez-vous que cette URL est correcte
  private token: string | null = localStorage.getItem('token');
  private currentUser: UserDTO | null = null; // Stocker l'utilisateur courant

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials)
      .pipe(
        map(response => {
          if (response && response.token) {
            this.setToken(response.token);
          }
          return response;
        })
      );
  }

  setToken(token: string | null): void {
    this.token = token;
    localStorage.setItem('token', token as string);
    this.loadCurrentUser(); 
  }

  getToken(): string | null {
    return this.token;
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUser = null; 
  }

  getHeaders(): HttpHeaders {
    const headersConfig: { [key: string]: string } = {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    };

    if (this.token) {
      headersConfig['Authorization'] = `Bearer ${this.token}`;
    }

    return new HttpHeaders(headersConfig);
  }
  getCurrentUserId(): Observable<number> {
    const headers = this.getHeaders();
    return this.http.get<number>(`${this.apiUrl}/current-user-id`, { headers }).pipe(
      tap(userId => {
        console.log('Current User ID:', userId);
      })
    );
  }

  getCurrentUser(): Observable<UserDTO> {
    const headers = this.getHeaders();
    return this.http.get<UserDTO>(`${this.apiUrl}/current-user-id`, { headers });
  }

  private loadCurrentUser(): void {
    this.getCurrentUser().subscribe(user => {
      this.currentUser = user;
    });
  }
}
