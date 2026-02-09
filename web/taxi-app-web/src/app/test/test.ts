import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatComponent } from '../live-chat/chat/chat';
import { NavbarComponent } from '../shared/navbar/navbar';

@Component({
  selector: 'app-test',
  imports: [CommonModule, ChatComponent, NavbarComponent],
  templateUrl: './test.html',
  styleUrl: './test.css',
})
export class TestComponent {
  isChildOpen = false;

  openChild(): void {
    this.isChildOpen = true;
  }

  closeChild(): void {
    this.isChildOpen = false;
  }
}
