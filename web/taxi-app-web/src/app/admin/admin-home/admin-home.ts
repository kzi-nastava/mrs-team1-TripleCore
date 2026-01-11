import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { PanicAlert, PanicNotificationsComponent } from '../../panic-notifications/panic-notifications';
import { NotificationSoundService } from '../../services/notification-sound-service';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [
    MapComponent, 
    NavbarComponent,
    RouterModule, 
    CommonModule, 
    MatTooltipModule,
    PanicNotificationsComponent
  ],
  templateUrl: './admin-home.html',
  styleUrls: ['./admin-home.css'],
})
export class AdminHomeComponent implements OnInit {
  showNotifications = false;
  showResolvedAlerts = false;
  panicAlerts: PanicAlert[] = [];
  
  constructor(
    private router: Router,
    private soundService: NotificationSoundService
  ) {}

  ngOnInit() {
    this.loadPanicNotifications();
    this.requestNotificationPermission();
  }

  loadPanicNotifications() {
    this.panicAlerts = [
      {
        id: 1,
        driverName: 'Marko Marković',
        passengerName: 'Ana Anić',
        time: new Date(Date.now() - 1000 * 60 * 30),
        read: false,
        resolved: false,
        vehicle: 'Toyota Corolla',
        location: 'Bulevar kralja Aleksandra',
        licensePlate: 'BG123AB'
      },
      {
        id: 2,
        driverName: 'Petar Petrović',
        passengerName: 'Jovan Jovanović',
        time: new Date(Date.now() - 1000 * 60 * 15),
        read: false,
        resolved: true,
        vehicle: 'Opel Astra',
        location: 'Knez Mihailova',
        licensePlate: 'BG456CD'
      }
    ];
    
    // If theres new unread alerts, play sound
    const unreadCount = this.panicAlerts.filter(a => !a.read && !a.resolved).length;
    if (unreadCount > 0) {
      setTimeout(() => this.soundService.play(), 1000);
    }
  }

  simulateNewPanic() {
    const drivers = ['Dragan Draganić', 'Petar Petrović', 'Ivan Ilić', 'Nikola Nikolić'];
    const passengers = ['Sara Sarić', 'Jovan Jovanović', 'Maja Majić', 'Ana Anić'];
    const vehicles = ['Toyota Corolla', 'Opel Astra', 'VW Golf', 'Renault Clio'];
    const locations = ['Bulevar kralja Aleksandra', 'Knez Mihailova', 'Slavija', 'Zemun'];
    
    const newPanic: PanicAlert = {
      id: Date.now(),
      driverName: drivers[Math.floor(Math.random() * drivers.length)],
      passengerName: passengers[Math.floor(Math.random() * passengers.length)],
      time: new Date(),
      read: false,
      resolved: false,
      vehicle: vehicles[Math.floor(Math.random() * vehicles.length)],
      location: locations[Math.floor(Math.random() * locations.length)]
    };
    
    this.panicAlerts.unshift(newPanic);
    this.soundService.play();
    this.showBrowserNotification(newPanic);
  }

  onMarkAsResolved(alertId: number) {
    const alert = this.panicAlerts.find(a => a.id === alertId);
    if (alert) {
      alert.resolved = true;
      alert.read = true;
    }
  }

  onMarkAsUnresolved(alertId: number) {
    const alert = this.panicAlerts.find(a => a.id === alertId);
    if (alert) {
      alert.resolved = false;
    }
  }

  onViewDetails(alertId: number) {
    console.log('Viewing alert:', alertId);
    this.showNotifications = false;
  }

  onToggleResolvedView(showResolved: boolean) {
    this.showResolvedAlerts = showResolved;
  }

  onMarkAllAsRead() {
    this.panicAlerts = this.panicAlerts.map(alert => ({
      ...alert,
      read: true
    }));
  }

  simulatePanicAlert() {
    this.simulateNewPanic();
  }

  toggleSound() {
    this.soundService.toggleMute();
  }

  isSoundMuted(): boolean {
    return this.soundService.isSoundMuted();
  }

  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/login']);
    }
  }

  private showBrowserNotification(alert: PanicAlert) {
    if (!('Notification' in window) || Notification.permission !== 'granted') return;
    
    new Notification('🚨 PANIC ALERT', {
      body: `${alert.driverName} → ${alert.passengerName}\n📍 ${alert.location}`,
      icon: '/icons/panic-icon.png',
      silent: true
    });
  }

  requestNotificationPermission() {
    if ('Notification' in window && Notification.permission === 'default') {
      Notification.requestPermission();
    }
  }
}