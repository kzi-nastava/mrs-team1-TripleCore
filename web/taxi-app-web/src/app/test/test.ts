import { Component } from '@angular/core';
import { ActiveRideTrackingComponent } from '../active-ride-tracking/active-ride-tracking';
import { CommonModule } from '@angular/common';
import { ReviewFormComponent } from '../reviews/review-form/review-form';

@Component({
  selector: 'app-test',
  imports: [ActiveRideTrackingComponent, CommonModule, ReviewFormComponent],
  templateUrl: './test.html',
  styleUrl: './test.css',
})
export class TestComponent {
  isChildOpen = false;

  openChild(): void {
    this.isChildOpen = true;
  }

  closeChild(): void {
    this.isChildOpen = false;
  }
}
