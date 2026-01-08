import { Component } from '@angular/core';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';

@Component({
  selector: 'app-passenger-home',
  standalone: true,
  imports: [MapComponent, NavbarComponent, RouterModule, CommonModule, MatTooltipModule],
  templateUrl: './passenger-home.html',
  styleUrls: ['./passenger-home.css'],
})
export class PassengerHomeComponent {
  constructor(private router: Router) {}
  
  logout() {
  this.router.navigate(['/home']); 
  }
}
