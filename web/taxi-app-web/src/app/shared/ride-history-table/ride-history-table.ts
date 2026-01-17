import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatTableModule } from "@angular/material/table";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { RideDetailsResponse } from '../../models/ride-details-response';



@Component({
  selector: 'app-ride-history-table',
  imports: [MatTableModule, CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatDatepickerModule, MatNativeDateModule, RouterModule],
  templateUrl: './ride-history-table.html',
  styleUrls: ['./ride-history-table.css'],
})
export class RideHistoryTableComponent {

  @Input() rides: RideDetailsResponse[] = [];

  displayedColumns: string[] = [
    'pickup',
    'destination',
    'date',
    'startTime',
    'endTime',
    'price',
    'panic',
    'details'
  ];

  getPickup(ride: RideDetailsResponse): string {
    return ride.startLocation.address;
  }

  getDestination(ride: RideDetailsResponse): string {
    return ride.endLocation.address;
  }

  getDate(ride: RideDetailsResponse): string {
    return ride.startTime.split('T')[0];
  }

  getStartTime(ride: RideDetailsResponse): string {
    return ride.startTime.split('T')[1].substring(0, 5);
  }

  getEndTime(ride: RideDetailsResponse): string {
    return ride.endTime.split('T')[1].substring(0, 5);
  }
}
