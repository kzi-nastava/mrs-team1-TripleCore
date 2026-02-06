import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { NotificationsPageComponent } from '../../notifications/notifications-page/notifications-page';
import { NotificationResponse } from '../../models/notification-response';
import { NotificationService } from '../../services/notification-service';
import { LogoutService } from '../../services/auth-service/logout-service';


@Component({
  selector: 'app-passenger-notifications',
  standalone: true,
  imports: [NavbarComponent, NotificationsPageComponent],
  templateUrl: './passenger-notifications.html',
  styleUrls: ['./passenger-notifications.css'],
})
export class PassengerNotificationsComponent implements OnInit {
  notifications: NotificationResponse[] = [];

  constructor (
    private notificationService: NotificationService,
    private cdr: ChangeDetectorRef,
    private logoutService: LogoutService) {}

  ngOnInit(): void {
  const passengerId = Number(localStorage.getItem('userId'));

  if (isNaN(passengerId)) {
    return;
  }

  this.notificationService
    .getPassengerNotifications(passengerId)
    .subscribe({
      next: (notifications) => {
        this.notifications = [...notifications];
        console.log('Parent notifications:', notifications);
        this.cdr.detectChanges();
      },
      error: err => console.error(err)
    });
    
}

  onLogoutClick() {
      this.logoutService.logoutWithBackend();
  }

}
