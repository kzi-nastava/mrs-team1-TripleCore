import { Component } from '@angular/core';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [MapComponent, NavbarComponent, RouterModule, CommonModule, MatTooltipModule],
  templateUrl: './admin-home.html',
  styleUrls: ['./admin-home.css'],
})

export class AdminHomeComponent {

  constructor(private router: Router) {}
  
  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/login']);
    }
  }
}
