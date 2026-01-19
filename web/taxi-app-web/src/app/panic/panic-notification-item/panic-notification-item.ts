import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanicAlert } from '../../models/panic-alert';


@Component({
  selector: 'app-panic-notification-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panic-notification-item.html',
  styleUrls: ['./panic-notification-item.css']
})
export class PanicNotificationItemComponent {
  @Input() alert!: PanicAlert;

  @Output() markAsResolved = new EventEmitter<number>();
  @Output() viewDetails = new EventEmitter<number>();

  onMarkAsResolved() {
    this.markAsResolved.emit(this.alert.id); 
  }

  onViewDetails() {
    this.viewDetails.emit(this.alert.id);
  }
}
