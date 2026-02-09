import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../shared/navbar/navbar';
import { UserChatComponent } from '../live-chat/user-chat/user-chat';

@Component({
  selector: 'app-test',
  imports: [CommonModule, UserChatComponent, NavbarComponent],
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
