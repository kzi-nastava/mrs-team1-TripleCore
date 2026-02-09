import { Component, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatCard } from '@angular/material/card';
import { ChatResponse } from '../../models/chat-response';
import { CommonModule } from '@angular/common';
import { ChatComponent } from '../chat/chat';
import { ChatListComponent } from '../chat-list/chat-list';
import { ChatService } from '../../services/chat-service';

@Component({
  selector: 'app-admin-chat',
  standalone: true,
  imports: [MatCard, CommonModule, ChatComponent, ChatListComponent],
  templateUrl: './admin-chat.html',
  styleUrl: './admin-chat.css',
})
export class AdminChatComponent implements OnInit{
  chatList: ChatResponse[] = [];
  selectedChat?: ChatResponse | null = null;
  chatOpened: boolean = false;

  constructor(
    private chatService: ChatService,
    private cdr: ChangeDetectorRef) {}

  @Output() close = new EventEmitter<void>();

  closeSelf(): void {
    this.close.emit();
  }

  ngOnInit(): void {
    this.loadChats();
  }

  private loadChats(): void {
    this.chatService.getAllChats().subscribe({
      next: (chats) => {
        this.chatList = [...chats];
        console.log(chats);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading chats:', err);
      }
    });
  }

  openChat(chat: ChatResponse): void {
    this.selectedChat = chat;
    this.chatOpened = true;
  }

  closeChat(): void {
    this.chatOpened = false;
    this.selectedChat = null;
  }

}
