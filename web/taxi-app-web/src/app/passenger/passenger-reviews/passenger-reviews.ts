import { ChangeDetectorRef, Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { ReviewDTO } from '../../models/review-dto';
import { ReviewService } from '../../services/review-service/review-service';
import { LogoutService } from '../../services/auth-service/logout-service';
import { ReviewsPageComponent } from '../../reviews/reviews-page/reviews-page';

@Component({
  selector: 'app-passenger-reviews',
  standalone: true,
  imports: [NavbarComponent, ReviewsPageComponent],
  templateUrl: './passenger-reviews.html',
  styleUrl: './passenger-reviews.css',
})
export class PassengerReviewsComponent {
  constructor(
    private reviewService: ReviewService, 
    private cdr: ChangeDetectorRef,
    private logoutService: LogoutService) {}

  passengerId: number = localStorage.getItem('userId') ? parseInt(localStorage.getItem('userId')!) : 0;
  reviews: ReviewDTO[] = [];

  ngOnInit() {
    this.loadReviews();
  }

  loadReviews(){
    this.reviewService.getPassengerReviews(this.passengerId).subscribe({
      next: (reviews) => {
        this.reviews = reviews ?? [];
        this.cdr.detectChanges();
        console.log('Loaded passenger reviews:', this.reviews);
      },
      error: (err) => {
        console.error('Failed to load passenger reviews', err);
        this.reviews = [];
        this.cdr.detectChanges();
      }
    });
  }

  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }
}
