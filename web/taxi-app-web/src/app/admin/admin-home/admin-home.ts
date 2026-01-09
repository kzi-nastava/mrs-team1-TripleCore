import { Component, OnInit, HostListener } from '@angular/core';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [
    MapComponent, 
    NavbarComponent,
    RouterModule, 
    CommonModule, 
    MatTooltipModule
  ],
  templateUrl: './admin-home.html',
  styleUrls: ['./admin-home.css'],
})

export class AdminHomeComponent implements OnInit {

  constructor(private router: Router) {}

  showNotifications = false;
  unreadPanicCount: number = 2; 
  panicAlerts: any[] = [
    {
      id: 1,
      driverName: 'Marko Marković',
      passengerName: 'Ana Anić',
      time: new Date(),
      status: 'active'
    },
    {
      id: 2,
      driverName: 'Petar Petrović',
      passengerName: 'Jovan Jovanović',
      time: new Date(Date.now() - 1000 * 60 * 15), // 15 min ago
      status: 'active'
    }
  ];

  ngOnInit() {
    this.loadPanicNotifications();
  }

  loadPanicNotifications() {
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const dropdown = document.querySelector('.notifications-dropdown');
    
    if (dropdown && !dropdown.contains(target) && this.showNotifications) {
      this.showNotifications = false;
    }
  }

  viewDetails(alertId: number) {
    console.log('Viewing alert:', alertId);
    this.showNotifications = false;
  }
  
  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/login']);
    }
  }
}