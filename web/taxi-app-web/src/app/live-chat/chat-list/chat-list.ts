import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ChatResponse } from '../../models/chat-response';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-chat-list',
  imports: [CommonModule],
  templateUrl: './chat-list.html',
  styleUrl: './chat-list.css',
})
export class ChatListComponent {

  @Input() chats!: ChatResponse[];

  @Output() chatSelected = new EventEmitter<ChatResponse>();

  selectChat(chat: ChatResponse): void {
  this.chatSelected.emit(chat);
}

}
