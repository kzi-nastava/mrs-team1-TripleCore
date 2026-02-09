import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatResponse } from '../models/chat-response';

interface SaveUserMessageRequest {
  senderId: number;
  text: string;
}

interface SaveAdminMessageRequest {
  chatId: number;
  senderId: number;
  text: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private readonly baseUrl = 'http://localhost:8080/api/chats';

  constructor(private http: HttpClient) {}

  sendUserMessage(senderId: number, text: string): Observable<any> {
    const payload: SaveUserMessageRequest = { senderId, text };
    return this.http.post(`${this.baseUrl}/save-user-message`, payload, { responseType: 'text' });
  }

  sendAdminMessage(chatId: number, senderId: number, text: string): Observable<any> {
    const payload: SaveAdminMessageRequest = { chatId, senderId, text };
    return this.http.post(`${this.baseUrl}/save-admin-message`, payload, { responseType: 'text' });
  }

  getUserChat(userId: number): Observable<ChatResponse> {
    return this.http.get<ChatResponse>(`${this.baseUrl}/user/${userId}`);
  }

  getAllChats(): Observable<ChatResponse[]> {
    return this.http.get<ChatResponse[]>(`${this.baseUrl}/all`);
  }
}
