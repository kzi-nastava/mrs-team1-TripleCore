import { Component } from '@angular/core';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { OrderRideRegisteredUser } from '../order-ride-registered-user/order-ride-registered-user';
import { LogoutService } from '../../services/auth-service/logout-service';

@Component({
  selector: 'app-passenger-home',
  standalone: true,
  imports: [MapComponent, NavbarComponent, RouterModule, CommonModule, MatTooltipModule, OrderRideRegisteredUser],
  templateUrl: './passenger-home.html',
  styleUrls: ['./passenger-home.css'],
})
export class PassengerHomeComponent {
  constructor(private router: Router, private logoutService: LogoutService) {}
  
  canOrderRide: boolean = true;

  handleRideOrdered(){
    this.canOrderRide = false;
  }

  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }
}
