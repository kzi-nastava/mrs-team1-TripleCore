import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { PanicAlert } from '../../models/panic-alert';
import { Router, RouterModule } from '@angular/router';
import { NotificationSoundService } from '../../services/notification-sound-service';
import { LogoutService } from '../../services/auth-service/logout-service';
import { AdminPanicService } from '../../services/admin-service/admin-panic-service';
import { AdminChatComponent } from '../../live-chat/admin-chat/admin-chat';
import { PanicNotificationsComponent } from '../../panic/panic-notifications/panic-notifications';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserBlockedResponse } from '../../models/user-blocked-response';
import { AdminBlockService } from '../../services/admin-service/admin-block-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-block-user',
  imports: [    
    NavbarComponent,
    RouterModule,
    CommonModule,
    MatTooltipModule,
    PanicNotificationsComponent,
    AdminChatComponent,
    FormsModule
  ],
  templateUrl: './block-user.html',
  styleUrl: './block-user.css',
})
export class BlockUserComponent implements OnInit {
  
    constructor(
    private router: Router,
    private soundService: NotificationSoundService,
    private logoutService: LogoutService,
    private panicService: AdminPanicService,
    private adminBlockService: AdminBlockService,
    private cdr: ChangeDetectorRef
  ) {}
  
  users: UserBlockedResponse[] = [];
  blockNotes: { [userId: number]: string } = {};

  loadUsers() {
    this.adminBlockService.getNonAdminUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load users', err)
    });
  }

  blockUser(user: UserBlockedResponse) {

    const note = this.blockNotes[user.id];

    this.adminBlockService.blockUser(user.id, note).subscribe({
      next: (updatedUser) => {


        user.blocked = true;


        this.blockNotes[user.id] = '';

        console.log("User blocked successfully");
  

      },
      error: (err) => {
        console.error("Failed to block user", err);
      }
    });

  }

 // navbar functionlities

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



  ngOnInit() {
    this.loadPanicNotifications();
    this.requestNotificationPermission();
    this.loadUsers();
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



}
