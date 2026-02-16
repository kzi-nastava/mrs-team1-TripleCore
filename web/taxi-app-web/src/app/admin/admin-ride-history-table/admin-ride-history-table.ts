import { Component, AfterViewInit, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort, Sort } from '@angular/material/sort';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
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
    MatButtonModule,
    MatIconModule,
    MatTooltipModule
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
        console.log('Loaded rides:', rides);
        this.originalRides = rides;
        this.rides.data = rides;
      },
      error: (err) => console.error('Failed to load rides', err)
    });
  }

  applyFilters() {
    console.log('Applying filters - fromDate:', this.fromDate, 'toDate:', this.toDate, 'search:', this.searchText);
    
    let filtered = [...this.originalRides];

    // search
    if (this.searchText && this.searchText.trim() !== '') {
      const search = this.searchText.toLowerCase().trim();
      filtered = filtered.filter(r => {
        const startAddr = r.startLocation?.address?.toLowerCase() || '';
        const endAddr = r.endLocation?.address?.toLowerCase() || '';
        return startAddr.includes(search) || endAddr.includes(search);
      });
    }

    // date filter
    if (this.fromDate || this.toDate) {
      filtered = filtered.filter(r => {
        const rideDate = new Date(r.startTime);
        console.log('Ride date:', rideDate);
        
        const rideDateStart = new Date(rideDate);
        rideDateStart.setHours(0, 0, 0, 0);

        if (this.fromDate) {
          const fromDateStart = new Date(this.fromDate);
          fromDateStart.setHours(0, 0, 0, 0);
          console.log('From date start:', fromDateStart, 'Ride date start:', rideDateStart);
          
          if (rideDateStart < fromDateStart) {
            console.log('Ride excluded - before from date');
            return false;
          }
        }

        if (this.toDate) {
          const toDateStart = new Date(this.toDate);
          toDateStart.setHours(0, 0, 0, 0);
          const toDateEnd = new Date(toDateStart);
          toDateEnd.setDate(toDateEnd.getDate() + 1);
          
          if (rideDateStart >= toDateEnd) {
            console.log('Ride excluded - after to date');
            return false;
          }
        }

        return true;
      });
    }

    console.log('Filtered results count:', filtered.length);
    this.rides.data = filtered;
  }

  clearFilters() {
    this.searchText = '';
    this.fromDate = null;
    this.toDate = null;
    this.rides.data = this.originalRides;
  }

  sortData(sort: Sort): void {
    const data = this.rides.data.slice();
    
    if (!sort.active || sort.direction === '') {
      this.rides.data = data.sort((a, b) => 
        new Date(b.startTime).getTime() - new Date(a.startTime).getTime()
      );
      return;
    }

    this.rides.data = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'route':
          return this.compare(
            (a.startLocation?.address || '') + (a.endLocation?.address || ''), 
            (b.startLocation?.address || '') + (b.endLocation?.address || ''), 
            isAsc
          );
        case 'startDate':
          return this.compare(
            new Date(a.startTime).getTime(), 
            new Date(b.startTime).getTime(), 
            isAsc
          );
        case 'endDate':
          return this.compare(
            new Date(a.endTime || 0).getTime(), 
            new Date(b.endTime || 0).getTime(), 
            isAsc
          );
        case 'cancelled':
          const aCancelled = a.cancelledBy ? true : false;
          const bCancelled = b.cancelledBy ? true : false;
          return this.compare(aCancelled, bCancelled, isAsc);
        case 'cancelledBy':
          return this.compare(a.cancelledBy || '', b.cancelledBy || '', isAsc);
        case 'price':
          return this.compare(a.price, b.price, isAsc);
        case 'panic':
          return this.compare(a.panic || false, b.panic || false, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string | boolean, b: number | string | boolean, isAsc: boolean): number {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
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