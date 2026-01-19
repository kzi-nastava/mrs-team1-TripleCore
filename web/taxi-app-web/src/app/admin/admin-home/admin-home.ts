import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MapComponent } from '../../map/map';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { PanicNotificationsComponent } from '../../panic/panic-notifications/panic-notifications';
import { NotificationSoundService } from '../../services/notification-sound-service';
import { LogoutService } from '../../services/auth-service/logout-service';
import { PanicAlert } from '../../models/panic-alert';
import { AdminPanicService } from '../../services/admin-service/admin-panic-service';

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
    private soundService: NotificationSoundService,
    private logoutService: LogoutService,
    private panicService: AdminPanicService
  ) {}

  ngOnInit() {
    this.loadPanicNotifications();
    this.requestNotificationPermission();
  }

  loadPanicNotifications() {
    this.panicService.getAllPanics().subscribe({
      next: (alerts) => {
        this.panicAlerts = alerts;

        const activeAlerts = alerts.filter(a => !a.resolved);

        const newActiveAlerts = activeAlerts.filter(
          a => !this.panicAlerts.some(old => old.id === a.id)
        );

        if (newActiveAlerts.length > 0) {
          this.soundService.play();
          newActiveAlerts.forEach(alert => this.showBrowserNotification(alert));
        }
      },
      error: (err) => console.error('Failed to load panic alerts', err)
    });
  }

  onMarkAsResolved(alertId: number) {
    this.panicService.resolvePanic(alertId).subscribe({
      next: () => {
        const alert = this.panicAlerts.find(a => a.id === alertId);
        if (alert) {
          alert.resolved = true;
        }
      },
      error: (err) => console.error('Failed to resolve panic', err)
    });
  }

  onViewDetails(alertId: number) {
    console.log('Viewing alert:', alertId);
    this.showNotifications = false;
  }

  onToggleResolvedView(showResolved: boolean) {
    this.showResolvedAlerts = showResolved;
  }

  toggleSound() {
    this.soundService.toggleMute();
  }

  isSoundMuted(): boolean {
    return this.soundService.isSoundMuted();
  }

  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }

  private showBrowserNotification(alert: PanicAlert) {
    if (!('Notification' in window) || Notification.permission !== 'granted') return;

    new Notification('🚨 PANIC ALERT', {
      body: `${alert.driverName} → ${alert.passengerName}\n📍 ${alert.location}`,
      icon: '/icons/panic-icon.png',
      silent: true
    });
  }

  private requestNotificationPermission() {
    if ('Notification' in window && Notification.permission === 'default') {
      Notification.requestPermission();
    }
  }
}
