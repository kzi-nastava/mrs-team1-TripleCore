import { Component, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { ChatComponent } from '../chat/chat';
import { MatCardModule } from '@angular/material/card';
import { ChatResponse } from '../../models/chat-response';
import { ChatService } from '../../services/chat-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-chat',
  standalone: true,
  imports: [ChatComponent, MatCardModule, CommonModule], 
  templateUrl: './user-chat.html',
  styleUrls: ['./user-chat.css'],        
})
export class UserChatComponent implements OnInit {

  @Output() close = new EventEmitter<void>();

  chat!: ChatResponse;          // ovde će biti chat sa backend-a
  currentUserId = Number(localStorage.getItem('userId'))

  constructor(
    private chatService: ChatService,
    private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadChat();
  }

  closeSelf(): void {
    this.close.emit();
  }

  private loadChat(): void {
    this.chatService.getUserChat(this.currentUserId).subscribe({
      next: (chat: ChatResponse) => {
        this.chat = chat;     
        console.log('Backend:', chat);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading chat:', err);
        this.chat = {
          chatId: 0,
          userId: this.currentUserId,
          userName: '' ,
          messages: []
        };
      }
    });
  }
}
