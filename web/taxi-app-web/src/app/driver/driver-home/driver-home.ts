import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { LogoutService } from '../../services/auth-service/logout-service';
import { DriverAvailabilityService } from '../../services/driver-service/driver-availability-service';
import { DriverStatusService } from '../../services/driver-service/driver-status-service';
import { UserChatComponent } from '../../live-chat/user-chat/user-chat';

@Component({
  selector: 'app-driver-home',
  standalone: true,
  imports: [
    CommonModule,
    MapComponent,
    NavbarComponent,
    MatTooltipModule,
    RouterModule,
    UserChatComponent
  ],
  templateUrl: './driver-home.html',
  styleUrls: ['./driver-home.css']
})
export class DriverHomeComponent implements OnInit {

  chatOpened: boolean = false;
  openChat(){
    this.chatOpened = true;
  }
  closeChat(){
    this.chatOpened = false;
  }

  isActive: boolean = true;
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private logoutService: LogoutService,
    private driverAvailabilityService: DriverAvailabilityService,
    private driverStatusService: DriverStatusService
  ) {}

  ngOnInit(): void {
    this.isActive = this.driverStatusService.isActive();
  }

  private getDriverId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
  }

  onLogoutClick(): void {
    this.logoutService.logoutWithBackend();
  }

  toggleActive(): void {
    this.isLoading = true;

    const driverId = this.getDriverId();
    const newStatus = !this.isActive;

    this.driverAvailabilityService
      .changeAvailability(driverId, newStatus)
      .subscribe({
        next: (response: string) => {
          this.driverStatusService.setActive(newStatus);

          this.isActive = newStatus;
          this.isLoading = false;

          alert(response);
        },
        error: (error) => {
          this.isLoading = false;

          alert('Error: ' + (error.error || 'Failed to change status'));
          console.error('Error changing driver availability:', error);
        }
      });
  }
}
