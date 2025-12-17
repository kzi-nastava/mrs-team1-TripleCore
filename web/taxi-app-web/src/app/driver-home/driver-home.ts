import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapComponent } from '../map/map';
import { NavbarComponent } from '../shared/navbar/navbar';
import { Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-driver-home',
  standalone: true,
  imports: [CommonModule, MapComponent, NavbarComponent, MatTooltipModule],
  templateUrl: './driver-home.html',
  styleUrls: ['./driver-home.css']
})
export class DriverHomeComponent {
  isActive: boolean = true;

  constructor(private router: Router) {}
  
  logout() {
  this.router.navigate(['/home']); 
  }

  toggleActive() {
    this.isActive = !this.isActive;

    alert(this.isActive ? 'You are now active' : 'You are now inactive');
  }
}
