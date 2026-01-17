import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

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
export class RideHistoryTableComponent implements OnChanges {

  @Input() rides: RideDetailsResponse[] = [];
  filteredRides: RideDetailsResponse[] = [];

  constructor(private router: Router) {}

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

  ngOnChanges(changes: SimpleChanges) {
    if (changes['rides'] && this.rides) {
      this.filteredRides = [...this.rides];
    }
  }

  viewDetails(ride: RideDetailsResponse): void {
    this.router.navigate(['/ride-details'], {
      state: { ride }
    });
  }

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

  textFilter: string = '';
  fromDateFilter: Date | null = null;
  toDateFilter: Date | null = null;

  clearFilters(): void {
    this.textFilter = '';
    this.fromDateFilter = null;
    this.toDateFilter = null;

    this.filteredRides = [...this.rides];
  }

  applyFilters(): void {
  this.filteredRides = this.rides.filter(ride => {
    // Text filter
    const combined = [
      ride.ordererName ?? '',
      ride.driverName ?? '',
      ride.startLocation.address ?? '',
      ride.endLocation.address ?? ''
    ].join(' ').toLowerCase();

    const textMatch = combined.includes(this.textFilter.toLowerCase());

    // Date range filter
    const start = new Date(ride.startTime);
    const end = new Date(ride.endTime);

    let fromMatch = true;
    let toMatch = true;

    if (this.fromDateFilter) {
      const from = new Date(this.fromDateFilter);
      from.setHours(0, 0, 0, 0); 
      toMatch = end >= from;
    }

    if (this.toDateFilter) {
      const to = new Date(this.toDateFilter);
      to.setHours(23, 59, 59, 999); 
      fromMatch = start <= to;
    }

    return textMatch && fromMatch && toMatch;
  });
}
}
