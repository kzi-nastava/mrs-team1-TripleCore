import { Component, Input } from '@angular/core';
import { ReviewDTO } from '../../models/review-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reviews-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reviews-page.html',
  styleUrls: ['./reviews-page.css'],
})
export class ReviewsPageComponent {
  stars = [1, 2, 3, 4, 5];

  @Input() reviews: ReviewDTO[] = [];
  
  ngOnInit() {
    console.log(this.reviews);
  }
}
