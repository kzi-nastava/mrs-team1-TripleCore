import { Component, Inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-driver-cancel-ride-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule
  ],
  providers: [DatePipe],
  templateUrl: './driver-cancel-ride-dialog.html'
})
export class DriverCancelRideDialogComponent {
  selectedReason: string = '';
  additionalNotes: string = '';

  constructor(
    public dialogRef: MatDialogRef<DriverCancelRideDialogComponent>,

    // Injected data from the parent component - rides
    @Inject(MAT_DIALOG_DATA) public data: any,
    private datePipe: DatePipe  // For formatting dates
  ) {}

  confirmCancel(): void {
    if (!this.isValid()) {
      return;
    }

    const result = {
      reason: this.selectedReason,
      notes: this.selectedReason === 'OTHER' ? this.additionalNotes : this.selectedReason,
      timestamp: new Date().toISOString()
    };
    
    this.dialogRef.close(result);
  }

  private isValid(): boolean {
    if (!this.selectedReason) {
      return false;
    }
    
    if (this.selectedReason === 'OTHER' && !this.additionalNotes.trim()) {
      return false;
    }
    
    return true;
  }
}