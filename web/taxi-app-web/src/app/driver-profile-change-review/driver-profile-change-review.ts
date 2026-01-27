import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PanicNotificationsComponent } from '../panic/panic-notifications/panic-notifications';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../shared/navbar/navbar';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PanicAlert } from '../models/panic-alert';
import { ChangeProfileRequestService } from '../services/change-profile-request-service/change-profile-request-service';
import { DriverProfileChangeRequestDetails } from '../services/change-profile-request-service/change-profile-request-service';
import { AdminPanicService } from '../services/admin-service/admin-panic-service';
import { LogoutService } from '../services/auth-service/logout-service';
import { NotificationSoundService } from '../services/notification-sound-service';

@Component({
  selector: 'app-driver-profile-change-review',
  imports: [NavbarComponent, RouterModule, CommonModule, MatTooltipModule, PanicNotificationsComponent],
  templateUrl: './driver-profile-change-review.html',
  styleUrl: './driver-profile-change-review.css',
})
export class DriverProfileChangeReviewComponent implements OnInit {
    showNotifications = false;
    showResolvedAlerts = false;
    panicAlerts: PanicAlert[] = [];
  
    requestId!: number;
    requestDetails!: DriverProfileChangeRequestDetails

    constructor(
    private router: Router,
    private route: ActivatedRoute,
    private soundService: NotificationSoundService,
    private logoutService: LogoutService,
    private panicService: AdminPanicService,
    private changeProfileRequestService: ChangeProfileRequestService,
    private cd: ChangeDetectorRef
  ) {}

    ngOnInit() {
    this.loadPanicNotifications();
    this.requestNotificationPermission();

         const param = this.route.snapshot.paramMap.get('requestId');
    console.log('Raw route param requestId:', param);

    this.requestId = Number(param);
    console.log('Parsed requestId as number:', this.requestId);

    this.loadRequestDetails();

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

  private loadRequestDetails() {
    this.requestId = Number(this.route.snapshot.paramMap.get('requestId'));
    console.log('Route param requestId:', this.requestId);
    if (!this.requestId) {
      console.error('Request ID not found in route');
      return;
    }

    this.changeProfileRequestService.getRequestDetails(this.requestId)
      .subscribe({
        next: (res) => {
          console.log('Request details loaded:', res);
          this.requestDetails = res;
          this.cd.detectChanges();
        },
        error: (err) => console.error('Failed to load request details', err)
      });
  }

  isActionInProgress: boolean = false;


  approveRequest() {
    if (!this.requestDetails || this.isActionInProgress) return;

    this.isActionInProgress = true;

    this.changeProfileRequestService.approveRequest(this.requestDetails.requestId).subscribe({
      next: () => {
        console.log('Request approved:', this.requestDetails.requestId);
        this.requestDetails.status = 'APPROVED';
        this.cd.detectChanges(); 
      },
      error: (err) => {
        console.error('Failed to approve request', err)
        this.isActionInProgress = false;
      }
    });
  }

  rejectRequest() {
    if (!this.requestDetails || this.isActionInProgress) return;

    this.isActionInProgress = true;

    this.changeProfileRequestService.rejectRequest(this.requestDetails.requestId).subscribe({
      next: () => {
        console.log('Request rejected:', this.requestDetails.requestId);
        this.requestDetails.status = 'REJECTED';
        this.cd.detectChanges(); 
      },
      error: (err) => {
        console.error('Failed to reject request', err);
        this.isActionInProgress = false;
      }
      });

    }


}
