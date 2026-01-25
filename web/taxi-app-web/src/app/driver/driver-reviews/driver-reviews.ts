import { ChangeDetectorRef, Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { ReviewsPageComponent } from '../../reviews/reviews-page/reviews-page';
import { ReviewDTO } from '../../models/review-dto';
import { ReviewService } from '../../services/review-service/review-service';

@Component({
  selector: 'app-driver-reviews',
  imports: [NavbarComponent, ReviewsPageComponent],
  templateUrl: './driver-reviews.html',
  styleUrl: './driver-reviews.css',
})
export class DriverReviewsComponent {
  // this will be loaded dynamically, for now hardcoding
  driverId: number = 1;
  reviews: ReviewDTO[] = [];

  constructor(private reviewService: ReviewService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadReviews();
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
