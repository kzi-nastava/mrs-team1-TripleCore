import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanicNotificationItemComponent } from '../panic-notification-item/panic-notification-item';
import { PanicAlert } from '../../models/panic-alert';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-panic-notifications',
  standalone: true,
  imports: [CommonModule, PanicNotificationItemComponent, RouterLink],
  templateUrl: './panic-notifications.html',
  styleUrls: ['./panic-notifications.css']
})
export class PanicNotificationsComponent implements OnInit {
  @Input() alerts: PanicAlert[] = [];
  @Input() showDropdown = false;
  @Input() showResolved = false;

  @Output() toggleDropdown = new EventEmitter<void>();
  @Output() markAsResolved = new EventEmitter<number>();
  @Output() viewDetails = new EventEmitter<number>();
  @Output() toggleResolvedView = new EventEmitter<boolean>();

  activeCount = 0;
  resolvedCount = 0;

  ngOnInit() {
    this.updateCounts();
  }

  ngOnChanges() {
    this.updateCounts();
  }

  updateCounts() {
    this.activeCount = this.alerts.filter(a => !a.resolved).length;
    this.resolvedCount = this.alerts.filter(a => a.resolved).length;
  }

  onToggleResolvedView() {
    this.showResolved = !this.showResolved;
    this.toggleResolvedView.emit(this.showResolved);
  }

  onMarkAsResolved(alertId: number) {
    this.markAsResolved.emit(alertId);
  }

  onViewDetails(alertId: number) {
    this.viewDetails.emit(alertId);
  }

  onToggleDropdown() {
    this.toggleDropdown.emit();
  }

  getFilteredAlerts(): PanicAlert[] {
    return this.showResolved 
      ? this.alerts.filter(a => a.resolved)
      : this.alerts.filter(a => !a.resolved);
  }
}
