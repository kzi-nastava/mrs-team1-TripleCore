import { ChangeDetectorRef, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationResponse } from '../../models/notification-response';
import { NotificationPopupComponent } from '../notification-popup/notification-popup';

@Component({
  selector: 'app-notifications-page',
  imports: [ CommonModule, NotificationPopupComponent ],
  standalone: true,
  templateUrl: './notifications-page.html',
  styleUrl: './notifications-page.css',
})
export class NotificationsPageComponent implements OnChanges {
  isChildOpen = false;
  selectedNotification!: NotificationResponse;

  @Input() notifications: NotificationResponse[] = [];

  constructor (private cdr: ChangeDetectorRef) {}

  openChild(notification: NotificationResponse): void {
    this.selectedNotification = notification;
    this.isChildOpen = true;
  }

  closeChild(): void {
    this.isChildOpen = false;
    this.selectedNotification.seen = true;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['notifications']) {
      
      this.notifications.sort((a, b) => {
        const dateA = new Date(a.time).getTime();
        const dateB = new Date(b.time).getTime();

        return dateB - dateA; 
      });
      console.log('Notifications updated:', this.notifications);
    }
  }
}
