import { Component, AfterViewInit, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { AdminRidesService } from '../../services/admin-service/admin-rides-service';

@Component({
  selector: 'app-admin-ride-history-table',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatTableModule,
    MatSortModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule
  ],
  templateUrl: './admin-ride-history-table.html',
  styleUrls: ['./admin-ride-history-table.css']
})
export class AdminRideHistoryTableComponent implements OnInit, AfterViewInit {

  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = [
    'route',
    'startDate',
    'endDate',
    'cancelled',
    'cancelledBy',
    'price',
    'panic',
    'details'
  ];

  rides = new MatTableDataSource<any>([]);
  originalRides: any[] = [];

  fromDate: Date | null = null;
  toDate: Date | null = null;
  searchText: string = '';

  constructor(private adminRidesService: AdminRidesService) {}

  ngOnInit() {
    this.loadRides();
  }

  ngAfterViewInit() {
    this.rides.sort = this.sort;
  }

  loadRides() {
    this.adminRidesService.getAllRides().subscribe({
      next: (rides) => {
        this.originalRides = rides;
        this.rides.data = rides;
      },
      error: (err) => console.error('Failed to load rides', err)
    });
  }

  applyFilters() {
    let filtered = [...this.originalRides];

    // search
    if (this.searchText) {
      const search = this.searchText.toLowerCase();
      filtered = filtered.filter(r =>
        r.startLocation?.address?.toLowerCase().includes(search) ||
        r.endLocation?.address?.toLowerCase().includes(search)
      );
    }

    // date filter
    if (this.fromDate || this.toDate) {
      filtered = filtered.filter(r => {
        const start = new Date(r.startTime);

        if (this.fromDate && start < this.startOfDay(this.fromDate)) {
          return false;
        }

        if (this.toDate && start > this.endOfDay(this.toDate)) {
          return false;
        }

        return true;
      });
    }

    this.rides.data = filtered;
  }

  clearFilters() {
    this.searchText = '';
    this.fromDate = null;
    this.toDate = null;
    this.rides.data = this.originalRides;
  }

  private startOfDay(date: Date): Date {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  private endOfDay(date: Date): Date {
    const d = new Date(date);
    d.setHours(23, 59, 59, 999);
    return d;
  }
}
