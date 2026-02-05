import { Component, Input } from '@angular/core';
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
export class NotificationsPageComponent {
  isChildOpen = false;
  selectedNotification!: NotificationResponse;

  notifications: any[] = [
  {
    id: 1,
    recipientId: 10,
    title: 'Vožnja završena',
    message: 'Vaša vožnja je uspešno završena. Ostavite ocenu za vozača.',
    link: 'review:101',
    time: '2026-02-03T18:45:00',
    seen: false
  },
  {
    id: 2,
    recipientId: 10,
    title: 'Aktivna vožnja',
    message: 'Vaša vožnja je u toku. Možete pratiti kretanje vozila uživo.',
    link: 'ride-tracking:202',
    time: '2026-02-03T17:30:00',
    seen: true
  },
  {
    id: 3,
    recipientId: 10,
    title: 'Podsetnik',
    message: 'Imate nepročitanu poruku vezanu za vašu rezervaciju.',
    link: 'ride-tracking:203',
    time: '2026-02-02T09:15:00',
    seen: false
  },
  {
    id: 4,
    recipientId: 10,
    title: 'Hvala što koristite aplikaciju',
    message: 'Vaša povratna informacija nam pomaže da unapredimo uslugu.',
    link: 'review:104',
    time: '2026-02-01T21:05:00',
    seen: true
  }
];


  openChild(notification: NotificationResponse): void {
    this.selectedNotification = notification;
    this.isChildOpen = true;
  }

  closeChild(): void {
    this.isChildOpen = false;
  }
}
