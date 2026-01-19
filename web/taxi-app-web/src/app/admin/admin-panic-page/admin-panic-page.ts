import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanicNotificationItemComponent } from '../../panic/panic-notification-item/panic-notification-item';
import { PanicAlert } from '../../models/panic-alert';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AdminPanicService } from '../../services/admin-service/admin-panic-service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-panic-page',
  standalone: true,
  imports: [
    CommonModule,
    PanicNotificationItemComponent,
    MatProgressSpinnerModule,
    RouterLink
  ],
  templateUrl: './admin-panic-page.html',
  styleUrls: ['./admin-panic-page.css']
})
export class AdminPanicPageComponent implements OnInit {

  panicAlerts: PanicAlert[] = [];
  loading = true;

  constructor(
    private panicService: AdminPanicService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadPanics();
  }

  loadPanics() {
    this.panicService.getAllPanics().subscribe({
      next: alerts => {
        this.panicAlerts = [...alerts].map(a => ({
          ...a,
          resolved: a.resolved === true || a.resolved
        }))
        .sort((a, b) => new Date(b.time).getTime() - new Date(a.time).getTime());

        this.loading = false;
        this.cdr.detectChanges(); // force render
      },
      error: err => {
        console.error('Failed to load panic alerts', err);
        this.loading = false;
      }
    });
  }

  onMarkAsResolved(alertId: number) {
    this.panicService.resolvePanic(alertId).subscribe({
      next: () => {
        const alert = this.panicAlerts.find(a => a.id === alertId);
        if (alert) alert.resolved = true;
      },
      error: err => console.error('Failed to resolve panic', err)
    });
  }

  onViewDetails(alertId: number) {
    console.log('Viewing alert', alertId);
  }

  get activeCount(): number {
    return this.panicAlerts.filter(a => !a.resolved).length;
  }

  get resolvedCount(): number {
    return this.panicAlerts.filter(a => a.resolved).length;
  }
}
