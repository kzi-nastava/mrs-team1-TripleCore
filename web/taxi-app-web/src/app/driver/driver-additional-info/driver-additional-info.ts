import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { UserInfoComponent } from '../../user-info/user-info';
import { AuthService } from '../../services/auth-service/logout-service';

@Component({
  selector: 'app-driver-additional-info',
  imports: [CommonModule, NavbarComponent, MatTooltipModule, RouterModule, UserInfoComponent],
  templateUrl: './driver-additional-info.html',
  styleUrl: './driver-additional-info.css',
})
export class DriverAdditionalInfo {
  isActive: boolean = true;

  activeLast24Hours: number = 8;

  vehicleModel: string = "Audi A3";
  vehicleType: string = "Standard";
  licencePlateNumber: string = "BG123-AB";
  numberOfSeats: number = 5;
  babyTransportAvailable: boolean = true;
  petsTransportAvailable: boolean = true;
  

  constructor(private router: Router, private authService: AuthService) {}
  
  onLogoutClick() {
    this.authService.logout();
  }

  toggleActive() {
    this.isActive = !this.isActive;

    alert(this.isActive ? 'You are now active' : 'You are now inactive');
  }
}
