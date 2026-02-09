import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { ChatResponse } from '../../models/chat-response';
import { MessageResponse } from '../../models/chat-response';
import { UserRole } from '../../models/chat-response';

import { Input } from '@angular/core';
import { ChatService } from '../../services/chat-service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './chat.html',
  styleUrls: ['./chat.css'],
})
export class ChatComponent implements AfterViewInit {

  @Input() chat!: ChatResponse;  
  currentUserId = Number(localStorage.getItem('userId'));
  currentUserRole = localStorage.getItem('role');

  constructor(private chatService: ChatService) {}

  newMessageText = '';

  @ViewChild('messagesContainer')
  private messagesContainer!: ElementRef<HTMLDivElement>;

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    if (this.messagesContainer) {
      const el = this.messagesContainer.nativeElement;
      el.scrollTop = el.scrollHeight;
    }
  }

  sendMessage(): void {
  if (!this.newMessageText?.trim()) return;

  const text = this.newMessageText;
  const senderId = this.currentUserId;

  if (this.currentUserRole === 'ADMIN') {
    this.chatService.sendAdminMessage(this.chat.chatId, senderId, text)
      .subscribe({
        next: () => {
          this.chat.messages.push({
            text,
            senderId,
            senderRole: UserRole.ADMIN,
            sentAt: new Date().toISOString()
          });
          this.newMessageText = '';
          setTimeout(() => this.scrollToBottom());
        },
        error: (err) => {
          console.error('Error sending admin message:', err);
        }
      });
  } else {
    this.chatService.sendUserMessage(senderId, text)
      .subscribe({
        next: () => {
          this.chat.messages.push({
            text,
            senderId,
            senderRole: this.currentUserRole == 'PASSENGER' ? UserRole.PASSENGER : UserRole.DRIVER, 
            sentAt: new Date().toISOString()
          });
          this.newMessageText = '';
          setTimeout(() => this.scrollToBottom());
        },
        error: (err) => {
          console.error('Error sending user message:', err);
        }
      });
  }
  setTimeout(() => this.scrollToBottom());
}


  
}
