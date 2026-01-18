import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { LogoutService } from '../../services/auth-service/logout-service';
import { DriverStatusService } from '../../services/driver-service/driver-status-service';
import { DriverAvailabilityService } from '../../services/driver-service/driver-availability-service';

@Component({
  selector: 'app-driver-additional-info',
  imports: [CommonModule, NavbarComponent, MatTooltipModule, RouterModule],
  templateUrl: './driver-additional-info.html',
  styleUrls: ['./driver-additional-info.css'],
})
export class DriverAdditionalInfoComponent {
  isActive: boolean = true;
  isLoading: boolean = false;

  activeLast24Hours: number = 8;

  vehicleModel: string = "Audi A3";
  vehicleType: string = "Standard";
  licencePlateNumber: string = "BG123-AB";
  numberOfSeats: number = 5;
  babyTransportAvailable: boolean = true;
  petsTransportAvailable: boolean = true;

  constructor(
    private router: Router,
     private logoutService: LogoutService,
     private driverStatusService: DriverStatusService,
     private driverAvailabilityService: DriverAvailabilityService) {}
  
  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }

  get canLogout(): boolean {
    return !this.isActive && !this.isLoading;
  }

  ngOnInit(): void {
    const driverId = this.getDriverId();
    this.isActive = this.driverStatusService.isActive();
  }

  getDriverId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
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
