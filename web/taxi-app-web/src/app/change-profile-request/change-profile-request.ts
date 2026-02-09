import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminPanicService } from '../services/admin-service/admin-panic-service';
import { PanicAlert } from '../models/panic-alert';
import { LogoutService } from '../services/auth-service/logout-service';
import { NotificationSoundService } from '../services/notification-sound-service';
import { Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../shared/navbar/navbar';
import { CommonModule } from '@angular/common';
import { MatTooltip, MatTooltipModule } from '@angular/material/tooltip';
import { PanicNotificationsComponent } from '../panic/panic-notifications/panic-notifications';
import { ChangeProfileRequestService, DriverProfileChangeRequest } from '../services/change-profile-request-service/change-profile-request-service';
import { DriverProfileResponse } from '../models/driver-profile-response';
import { AdminChatComponent } from '../live-chat/admin-chat/admin-chat';


@Component({
  selector: 'app-change-profile-request',
  standalone: true,
  imports: [NavbarComponent, RouterModule, CommonModule, MatTooltipModule, PanicNotificationsComponent, AdminChatComponent],
  templateUrl: './change-profile-request.html',
  styleUrl: './change-profile-request.css',
})
export class ChangeProfileRequestComponent implements OnInit {
  chatOpened: boolean = false;
  openChat(){
    this.chatOpened = true;
  }
  closeChat(){
    this.chatOpened = false;
  }

  showNotifications = false;
  showResolvedAlerts = false;
  panicAlerts: PanicAlert[] = [];

  driverProfileChangeRequests: DriverProfileChangeRequest[] = [];

  constructor(
    private router: Router,
    private soundService: NotificationSoundService,
    private logoutService: LogoutService,
    private panicService: AdminPanicService,
    private changeProfileRequestService: ChangeProfileRequestService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadPanicNotifications();
    this.requestNotificationPermission();
    this.loadProfileChangeRequests();
  }

  loadPanicNotifications() {
    this.panicService.getAllPanics().subscribe({
      next: (alerts) => {
        const activeAlerts = alerts.filter(a => !a.resolved);

        const newActiveAlerts = activeAlerts.filter(
          a => !this.panicAlerts.some(old => old.id === a.id)
        );

        this.panicAlerts = [...alerts]; 

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



goToReviewPage(requestId: number) {

  this.router.navigate(['/driver-profile-change-review', requestId]);
}

private loadProfileChangeRequests() {
  this.changeProfileRequestService.getDriverProfileRequests().subscribe({
    next: (requests) => {
       console.log('Received requests from backend:', requests);
      this.driverProfileChangeRequests = requests.map(req => ({
        ...req,
        createdAt: new Date(req.createdAt)
      }));
      this.cd.detectChanges();
    },
    error: (err) => console.error('Failed to load profile change requests', err)
  });
}

}
