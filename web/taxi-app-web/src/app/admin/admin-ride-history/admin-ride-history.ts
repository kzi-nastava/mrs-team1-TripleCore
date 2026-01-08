import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router, RouterModule } from '@angular/router';
import { AdminRideHistoryTableComponent } from '../admin-ride-history-table/admin-ride-history-table';
import { MatTooltip } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-ride-history',
  standalone: true,
  imports: [NavbarComponent, AdminRideHistoryTableComponent, MatTooltip, RouterModule, CommonModule],
  templateUrl: './admin-ride-history.html',
  styleUrls: ['./admin-ride-history.css'],
})

export class AdminRideHistoryComponent {
  constructor(private router: Router) {}

  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/login']);
    }
  }
}
