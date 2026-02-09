import { ChangeDetectorRef, Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { ReviewsPageComponent } from '../../reviews/reviews-page/reviews-page';
import { ReviewDTO } from '../../models/review-dto';
import { ReviewService } from '../../services/review-service/review-service';
import { DriverStatusService } from '../../services/driver-service/driver-status-service';
import { DriverAvailabilityService } from '../../services/driver-service/driver-availability-service';
import { LogoutService } from '../../services/auth-service/logout-service';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { UserChatComponent } from '../../live-chat/user-chat/user-chat';

@Component({
  selector: 'app-driver-reviews',
  imports: [NavbarComponent, ReviewsPageComponent, MatTooltipModule, RouterLink, UserChatComponent],
  templateUrl: './driver-reviews.html',
  styleUrl: './driver-reviews.css',
})
export class DriverReviewsComponent {
  driverId: number = localStorage.getItem('userId') ? Number(localStorage.getItem('userId')) : 0;
  reviews: ReviewDTO[] = [];

  isActive: boolean = true;
  isLoading: boolean = false;

  constructor(
    private reviewService: ReviewService, 
    private cdr: ChangeDetectorRef,
    private logoutService: LogoutService,
    private driverAvailabilityService: DriverAvailabilityService,
    private driverStatusService: DriverStatusService) {}

  ngOnInit() {
    this.loadReviews();
    this.isActive = this.driverStatusService.isActive();
  }

  private getDriverId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
  }

  onLogoutClick(): void {
    this.logoutService.logoutWithBackend();
  }

  toggleActive(): void {
    this.isLoading = true;

    const driverId = this.getDriverId();
    const newStatus = !this.isActive;

    this.driverAvailabilityService
      .changeAvailability(driverId, newStatus)
      .subscribe({
        next: (response: string) => {
          this.driverStatusService.setActive(newStatus);

          this.isActive = newStatus;
          this.isLoading = false;

          alert(response);
        },
        error: (error) => {
          this.isLoading = false;

          alert('Error: ' + (error.error || 'Failed to change status'));
          console.error('Error changing driver availability:', error);
        }
      });
  }

  loadReviews(){
    this.reviewService.getDriverReviews(this.driverId).subscribe({
      next: (reviews) => {
        this.reviews = reviews ?? [];
        this.cdr.detectChanges();
        console.log('Loaded driver reviews:', this.reviews);
      },
      error: (err) => {
        console.error('Failed to load driver reviews', err);
        this.reviews = [];
        this.cdr.detectChanges();
      }
    });
  }
}
