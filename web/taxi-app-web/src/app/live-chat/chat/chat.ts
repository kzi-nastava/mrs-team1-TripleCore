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
export class ChatComponent implements AfterViewInit{

  @Output() close = new EventEmitter<void>();

  closeSelf(): void {
    this.close.emit();
  }

  currentUserId = 42;
  newMessageText = '';

  chat: ChatResponse = {
  chatId: 1,
  userId: 42,
  userName: 'Mia Milic',
  messages: [
    {
      text: 'Zdravo, gde se trenutno nalazite?',
      senderId: 42,
      senderRole: UserRole.PASSENGER,
      sentAt: '2026-02-08T18:30:00'
    },
    {
      text: 'Na putu sam, stižem za 5 minuta.',
      senderId: 7,
      senderRole: UserRole.DRIVER,
      sentAt: '2026-02-08T18:31:10'
    },
    {
      text: 'U redu, čekam ispred zgrade.',
      senderId: 42,
      senderRole: UserRole.PASSENGER,
      sentAt: '2026-02-08T18:31:45'
    },
    {
      text: 'Vidim vas na mapi, parkiram se kod ulaza.',
      senderId: 7,
      senderRole: UserRole.DRIVER,
      sentAt: '2026-02-08T18:33:05'
    },
    {
      text: 'Super, nosim plavi kaput.',
      senderId: 42,
      senderRole: UserRole.PASSENGER,
      sentAt: '2026-02-08T18:33:40'
    },
    {
      text: 'Odlično, bela Toyota ispred zgrade.',
      senderId: 7,
      senderRole: UserRole.DRIVER,
      sentAt: '2026-02-08T18:34:10'
    },
    {
      text: 'Vidim vas, silazim sada.',
      senderId: 42,
      senderRole: UserRole.PASSENGER,
      sentAt: '2026-02-08T18:34:45'
    },
    {
      text: 'U redu, sačekajte pored ulaza.',
      senderId: 7,
      senderRole: UserRole.DRIVER,
      sentAt: '2026-02-08T18:35:20'
    }
  ]
};

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  @ViewChild('messagesContainer')
  private messagesContainer!: ElementRef<HTMLDivElement>;

  private scrollToBottom(): void {
    const el = this.messagesContainer.nativeElement;
    el.scrollTop = el.scrollHeight;
  }

  sendMessage(): void{
    const newMsg: MessageResponse = {
    text: this.newMessageText,
    senderId: this.currentUserId,
    senderRole: UserRole.PASSENGER, // po potrebi promeni
    sentAt: new Date().toISOString() // ISO string za LocalDateTime
    };

    this.chat.messages.push(newMsg);

    // Očisti input polje
    this.newMessageText = '';

    // Scroll na dno (ako koristiš scroll funkciju)
    setTimeout(() => this.scrollToBottom());
  }

}
