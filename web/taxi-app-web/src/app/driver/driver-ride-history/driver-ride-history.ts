import { ChangeDetectorRef, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RideHistoryTableComponent } from '../../shared/ride-history-table/ride-history-table';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { DriverService } from '../../services/driver-service';
import { DriverStatusService } from '../../services/driver-service/driver-status-service';
import { DriverAvailabilityService } from '../../services/driver-service/driver-availability-service';
import { LogoutService } from '../../services/auth-service/logout-service';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { UserChatComponent } from '../../live-chat/user-chat/user-chat';

@Component({
  selector: 'app-driver-ride-history',
  standalone: true,
  imports: [NavbarComponent, RideHistoryTableComponent, RouterModule, MatTooltipModule, RouterLink, UserChatComponent],
  templateUrl: './driver-ride-history.html',
  styleUrls: ['./driver-ride-history.css'],
})
export class DriverRideHistoryComponent {
  driverId: number = localStorage.getItem('userId') ? Number(localStorage.getItem('userId')) : 0;
  driverRideHistory: RideDetailsResponse[] = [];

  isActive: boolean = true;
  isLoading: boolean = false;

  constructor(
    private driverService: DriverService,
    // to detect changes from outside Angular zone (like data from HTTP)
    private cdr: ChangeDetectorRef,
    private logoutService: LogoutService,
    private driverAvailabilityService: DriverAvailabilityService,
    private driverStatusService: DriverStatusService) {}

  ngOnInit(): void {
    this.loadRideHistory();
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

  private loadRideHistory(): void {
    this.driverService.getRideHistory(this.driverId).subscribe({
      next: (rides) => {
        // if rides is null or undefined, default to empty array
        this.driverRideHistory = rides ?? [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load driver ride history', err);
        this.driverRideHistory = [];
        this.cdr.detectChanges();
      }
    });
  }
}