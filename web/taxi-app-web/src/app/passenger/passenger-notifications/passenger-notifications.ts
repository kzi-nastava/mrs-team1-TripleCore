import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { NotificationsPageComponent } from '../../notifications/notifications-page/notifications-page';


@Component({
  selector: 'app-passenger-notifications',
  imports: [NavbarComponent, NotificationsPageComponent],
  templateUrl: './passenger-notifications.html',
  styleUrls: ['./passenger-notifications.css'],
})
export class PassengerNotificationsComponent {

}
