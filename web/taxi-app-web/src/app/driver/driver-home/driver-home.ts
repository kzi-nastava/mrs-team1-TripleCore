import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { LogoutService } from '../../services/auth-service/logout-service';
import { DriverAvailabilityService } from '../../services/driver-service/driver-availability-service';

@Component({
  selector: 'app-driver-home',
  standalone: true,
  imports: [CommonModule, MapComponent, NavbarComponent, MatTooltipModule, RouterModule],
  templateUrl: './driver-home.html',
  styleUrls: ['./driver-home.css']
})
export class DriverHomeComponent implements OnInit { 
  isActive: boolean = true;
  isLoading: boolean = false; 

  constructor(
    private router: Router, 
    private logoutService: LogoutService,
    private driverAvailabilityService: DriverAvailabilityService 
  ) {}
  
  ngOnInit(): void {
    this.loadDriverStatus();
  }

  loadDriverStatus(): void {
    const driverId = this.getDriverId();
    
    if (driverId) {
      const storedStatus = localStorage.getItem('driverAvailable');
      this.isActive = storedStatus ? storedStatus === 'true' : true;
    }
  }

  getDriverId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
  }
  
  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }

  toggleActive() {   
    this.isLoading = true; 
    const driverId = this.getDriverId();
    const newStatus = !this.isActive;
    
    this.driverAvailabilityService.changeAvailability(driverId, newStatus).subscribe({
      next: (response: string) => {
        localStorage.setItem('driverAvailable', newStatus.toString());
        this.isActive = newStatus;
        this.isLoading = false;
        
        setTimeout(() => {
          alert(response);
        }, 0);
      },
      error: (error) => {
        this.isLoading = false;
        
        this.isActive = !newStatus;
        
        alert('Error: ' + (error.error || 'Failed to change status'));
        console.error('Error changing status:', error);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}