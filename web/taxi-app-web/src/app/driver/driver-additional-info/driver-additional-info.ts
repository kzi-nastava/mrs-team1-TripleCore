import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-driver-additional-info',
  imports: [CommonModule, NavbarComponent, MatTooltipModule, RouterModule],
  templateUrl: './driver-additional-info.html',
  styleUrl: './driver-additional-info.css',
})
export class DriverAdditionalInfoComponent {
  isActive: boolean = true;

  activeLast24Hours: number = 8;

  vehicleModel: string = "Audi A3";
  vehicleType: string = "Standard";
  licencePlateNumber: string = "BG123-AB";
  numberOfSeats: number = 5;
  babyTransportAvailable: boolean = true;
  petsTransportAvailable: boolean = true;
  

  constructor(private router: Router) {}
  
  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/login']);
    }
  }

  toggleActive() {
    this.isActive = !this.isActive;

    alert(this.isActive ? 'You are now active' : 'You are now inactive');
  }
}
