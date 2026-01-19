import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-review-form',
  imports: [MatCardModule, MatIconModule, MatFormFieldModule, MatInputModule, MatButtonModule, FormsModule],
  standalone: true,
  templateUrl: './review-form.html',
  styleUrls: ['./review-form.css'],
})
export class ReviewFormComponent {
  stars = [1, 2, 3, 4, 5];

  driverRating = 0;
  vehicleRating = 0;
  comment = '';

  setDriverRating(rating: number): void {
    this.driverRating = rating;
  }

  setVehicleRating(rating: number): void {
    this.vehicleRating = rating;
  }

  submitReview(): void {
    const review = {
      driverRating: this.driverRating,
      vehicleRating: this.vehicleRating,
      comment: this.comment
    };

    console.log('Review submitted:', review);

    // opcionalno resetovanje forme
    this.driverRating = 0;
    this.vehicleRating = 0;
    this.comment = '';
  }
}
