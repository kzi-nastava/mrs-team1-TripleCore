import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { Router, RouterModule } from '@angular/router';
import { AdminRideHistoryTableComponent } from '../admin-ride-history-table/admin-ride-history-table';
import { MatTooltip } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import { LogoutService } from '../../services/auth-service/logout-service';

@Component({
  selector: 'app-admin-ride-history',
  standalone: true,
  imports: [NavbarComponent, AdminRideHistoryTableComponent, MatTooltip, RouterModule, CommonModule],
  templateUrl: './admin-ride-history.html',
  styleUrls: ['./admin-ride-history.css'],
})

export class AdminRideHistoryComponent {
  constructor(private router: Router, private logoutService: LogoutService) {}

  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }
}