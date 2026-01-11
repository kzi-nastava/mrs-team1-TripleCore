import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanicNotificationItemComponent } from '../panic-notification-item/panic-notification-item';

export interface PanicAlert {
  id: number;
  driverName: string;
  passengerName: string;
  time: Date;
  read: boolean;
  resolved: boolean;
  vehicle: string;
  location: string;
  licensePlate?: string;
}

@Component({
  selector: 'app-panic-notifications',
  standalone: true,
  imports: [CommonModule, PanicNotificationItemComponent],
  templateUrl: './panic-notifications.html',
  styleUrls: ['./panic-notifications.css']
})
export class PanicNotificationsComponent implements OnInit {
  // gets from parent component - admin-home
  @Input() alerts: PanicAlert[] = [];
  @Input() showDropdown = false;
  @Input() showResolved = false;
  
  // returns to parent component - admin-home
  @Output() toggleDropdown = new EventEmitter<void>();
  @Output() markAsResolved = new EventEmitter<number>();
  @Output() markAsUnresolved = new EventEmitter<number>();
  @Output() viewDetails = new EventEmitter<number>();
  @Output() toggleResolvedView = new EventEmitter<boolean>();
  @Output() markAllAsRead = new EventEmitter<void>();

  unreadCount = 0;
  activeCount = 0;
  resolvedCount = 0;

  ngOnInit() {
    this.updateCounts();
  }

  ngOnChanges() {
    this.updateCounts();
  }

  updateCounts() {
    this.unreadCount = this.alerts.filter(a => !a.read && !a.resolved).length;
    this.activeCount = this.alerts.filter(a => !a.resolved).length;
    this.resolvedCount = this.alerts.filter(a => a.resolved).length;
  }

  onToggleResolvedView() {
    this.showResolved = !this.showResolved;
    this.toggleResolvedView.emit(this.showResolved);
  }

  onMarkAsResolved(id: number) {
    this.markAsResolved.emit(id);
  }

  onMarkAsUnresolved(id: number) {
    this.markAsUnresolved.emit(id);
  }

  onViewDetails(id: number) {
    this.viewDetails.emit(id);
  }

  onToggleDropdown() {
    if (this.showDropdown && this.unreadCount > 0) {
      this.markAllAsRead.emit();
    }
    this.toggleDropdown.emit();
  }

  getFilteredAlerts(): PanicAlert[] {
    return this.showResolved 
      ? this.alerts.filter(a => a.resolved)
      : this.alerts.filter(a => !a.resolved);
  }
}