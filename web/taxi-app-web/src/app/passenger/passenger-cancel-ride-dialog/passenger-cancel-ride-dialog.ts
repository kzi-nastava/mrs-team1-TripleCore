import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-passenger-cancel-ride-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './passenger-cancel-ride-dialog.html'
})
export class PassengerCancelRideDialogComponent implements OnInit {
  additionalNotes: string = '';
  minutesUntilRide: number = 0;
  canCancel: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<PassengerCancelRideDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    console.log('Dialog data:', this.data);
    
    if (this.data.minutesUntilRide !== undefined) {
      this.minutesUntilRide = this.data.minutesUntilRide;
    } else if (this.data.ride && this.data.ride.date) {
      const now = new Date();
      const rideTime = new Date(this.data.ride.date);
      const diffMs = rideTime.getTime() - now.getTime();
      this.minutesUntilRide = Math.floor(diffMs / (1000 * 60));
    }
    
    this.canCancel = this.minutesUntilRide > 10;
    
    console.log('Minutes until ride:', this.minutesUntilRide);
    console.log('Can cancel:', this.canCancel);
  }

  confirmCancel(): void {
    const result = {
      notes: this.additionalNotes,
      isLateCancellation: this.minutesUntilRide < 10
    };
    
    console.log('Dialog closing with:', result);
    this.dialogRef.close(result);
  }
}