import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationResponse } from '../../models/notification-response';

@Component({
  selector: 'app-notifications-page',
  imports: [ CommonModule ],
  standalone: true,
  templateUrl: './notifications-page.html',
  styleUrl: './notifications-page.css',
})
export class NotificationsPageComponent {
  notifications: NotificationResponse[] = [
  {
    id: 1,
    recipientId: 10,
    title: 'Vožnja završena',
    message: 'Vaša vožnja je uspešno završena. Ostavite ocenu za vozača.',
    link: 'make-review:101',
    time: '2026-02-03T18:45:00',
    seen: false
  },
  {
    id: 2,
    recipientId: 10,
    title: 'Aktivna vožnja',
    message: 'Vaša vožnja je u toku. Možete pratiti kretanje vozila uživo.',
    link: 'track-ride:202',
    time: '2026-02-03T17:30:00',
    seen: true
  },
  {
    id: 3,
    recipientId: 10,
    title: 'Podsetnik',
    message: 'Imate nepročitanu poruku vezanu za vašu rezervaciju.',
    link: 'track-ride:203',
    time: '2026-02-02T09:15:00',
    seen: false
  },
  {
    id: 4,
    recipientId: 10,
    title: 'Hvala što koristite aplikaciju',
    message: 'Vaša povratna informacija nam pomaže da unapredimo uslugu.',
    link: 'make-review:104',
    time: '2026-02-01T21:05:00',
    seen: true
  }
];

}
