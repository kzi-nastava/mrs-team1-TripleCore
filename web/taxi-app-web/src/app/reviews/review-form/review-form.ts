import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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

  stars = [1, 2, 3, 4, 5];

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
    const review = {
      driverRating: this.driverRating,
      vehicleRating: this.vehicleRating,
      comment: this.comment
    };

    console.log('Submitted review:', review);

    this.reset();
  }

  cancel(): void {
    this.reset();
  }

  private reset(): void {
    this.driverRating = 0;
    this.vehicleRating = 0;
    this.comment = '';
  }
}
