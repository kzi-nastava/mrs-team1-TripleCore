import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NotificationResponse } from '../../models/notification-response';
import { MatCard } from '@angular/material/card';
import { MatCardContent } from '@angular/material/card';
import { ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notification-popup',
  standalone: true,
  imports: [MatCard, MatCardContent, RouterLink, CommonModule],
  templateUrl: './notification-popup.html',
  styleUrl: './notification-popup.css',
})
export class NotificationPopupComponent implements OnInit{
  @Input() notification!: NotificationResponse;
  link: any[] = [];

  constructor (private cdr: ChangeDetectorRef){}

  @Output() close = new EventEmitter<void>();

  closeSelf(): void {
    this.close.emit();
  }

  ngOnInit(): void {
    if (this.notification.link) {
      const parts = this.notification.link.split(':');
      if (parts[0] == 'ride-tracking'){
        this.link = ['/active-ride-tracking', parts[1]];
      } else if (parts[0] == 'review'){
        this.link = ['passenger', 'passenger-my-rides']
      } else{
        this.link = [];
      }
    }
    this.cdr.detectChanges();
  }

}
