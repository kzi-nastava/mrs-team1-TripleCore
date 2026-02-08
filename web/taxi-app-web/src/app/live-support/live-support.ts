import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-live-support',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './live-support.html',
  styleUrl: './live-support.css',
})
export class LiveSupportComponent {
  role: String | null = localStorage.getItem("role");
  userId: number = Number(localStorage.getItem("userId"))
  
}
