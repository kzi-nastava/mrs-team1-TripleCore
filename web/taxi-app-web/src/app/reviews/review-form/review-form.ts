import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CreateReviewRequest } from '../../models/create-review-request';
import { ReviewService } from '../../services/review-service/review-service';

@Component({
  selector: 'app-review-form',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './review-form.html',
  styleUrls: ['./review-form.css'],
})
export class ReviewFormComponent {

  constructor(private reviewService: ReviewService) {}

  stars = [1, 2, 3, 4, 5];

  // passenger id 
  passengerId = 2;
  rideId = 1;
  // ride id

  driverRating = 1;
  vehicleRating = 1;
  comment = '';

  setDriverRating(rating: number): void {
    this.driverRating = rating;
  }

  setVehicleRating(rating: number): void {
    this.vehicleRating = rating;
  }

  isFormValid(): boolean {
    return this.driverRating > 0 && this.vehicleRating > 0;
  }

  submit(): void {
    const request: CreateReviewRequest = {
      passengerId: this.passengerId,
      rideId: this.rideId,
      driverRating: this.driverRating,
      vehicleRating: this.vehicleRating,
      comment: this.comment
    };

    this.reviewService.createReview(request).subscribe({
      next: message => {
        console.log('Backend says:', message);
        this.reset();
      },
      error: err => {
        console.error(err);
      }
    });
    this.reset();
  }

  cancel(): void {
    this.reset();
  }

  private reset(): void {
    this.driverRating = 1;
    this.vehicleRating = 1;
    this.comment = '';
  }
}
