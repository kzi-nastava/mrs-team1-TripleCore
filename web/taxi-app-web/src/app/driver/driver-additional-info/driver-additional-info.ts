import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { LogoutService } from '../../services/auth-service/logout-service';
import { DriverStatusService } from '../../services/driver-service/driver-status-service';
import { DriverAvailabilityService } from '../../services/driver-service/driver-availability-service';
import { UserProfileService } from '../../services/user-info-service/user-info-service';
import { DriverProfileResponse, VehicleType } from '../../models/driver-profile-response';
import { UserChatComponent } from '../../live-chat/user-chat/user-chat';

@Component({
  selector: 'app-driver-additional-info',
  imports: [CommonModule, NavbarComponent, MatTooltipModule, RouterModule, UserChatComponent],
  templateUrl: './driver-additional-info.html',
  styleUrls: ['./driver-additional-info.css'],
})
export class DriverAdditionalInfoComponent implements OnInit {
  chatOpened: boolean = false;
  openChat(){
    this.chatOpened = true;
  }
  closeChat(){
    this.chatOpened = false;
  }

  isActive: boolean = true;
  isLoading: boolean = false;

  workingHoursToday: number = 0;

  

  driverProfile: DriverProfileResponse | null = null;

  constructor(
    private router: Router,
     private logoutService: LogoutService,
     private driverStatusService: DriverStatusService,
     private driverAvailabilityService: DriverAvailabilityService,
     private userProfileService: UserProfileService
    ) {}
  
  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }

  get canLogout(): boolean {
    return !this.isActive && !this.isLoading;
  }

  ngOnInit(): void {
    const driverId = this.getDriverId();
    this.isActive = this.driverStatusService.isActive();

    const cahcedProfile = localStorage.getItem('driverProfile')
    if (cahcedProfile) {
      this.driverProfile = JSON.parse(cahcedProfile) as DriverProfileResponse;
      this.workingHoursToday = this.driverProfile.workingHoursToday;
    } else {
      this.userProfileService.getDriverProfile(driverId).subscribe({
        next: (profile) => {
          this.driverProfile = profile;
          this.workingHoursToday = profile.workingHoursToday;
          localStorage.setItem('driverProfile', JSON.stringify(profile));
        },
        error: (error) => {
          console.error('Error loading driver profile:', error);
        }
    });

    }
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

    public formatVehicleType(type: VehicleType | string): string {
    
      switch (type) {
        case VehicleType.STANDARD:
        case 'STANDARD':
          return 'Standard';
        case VehicleType.LUXURY:
        case 'LUXURY':
          return 'Luxury';
        case VehicleType.VAN:
        case 'VAN':
          return 'Van';
        default:
          return '';
      }
  }

}
