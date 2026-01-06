import { Component, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatDatepicker } from '@angular/material/datepicker';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-ride-history-table',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatSortModule,
    RouterModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './admin-ride-history-table.html',
  styleUrls: ['./admin-ride-history-table.css']
})
export class AdminRideHistoryTableComponent implements AfterViewInit {

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('fromPicker') fromPicker!: MatDatepicker<Date>;
  @ViewChild('toPicker') toPicker!: MatDatepicker<Date>;
  @ViewChild('searchInput', { static: false }) searchInput!: ElementRef;

  displayedColumns: string[] = [
    'route', 'startDate', 'endDate', 'cancelled', 'cancelledBy', 'price', 'panic', 'details'
  ];

  ridesData = [
    {
      id: 1,
      pickup: 'Novi Sad',
      destination: 'Beograd',
      startDate: new Date('2025-12-01T10:00'),
      endDate: new Date('2025-12-01T11:15'),
      cancelled: false,
      cancelledBy: null,
      price: 2500,
      panicTriggered: false
    },
    {
      id: 2,
      pickup: 'Beograd',
      destination: 'Niš',
      startDate: new Date('2025-12-03T08:00'),
      endDate: new Date('2025-12-03T08:30'),
      cancelled: true,
      cancelledBy: 'DRIVER',
      price: 0,
      panicTriggered: true
    },
    {
      id: 3,
      pickup: 'Subotica',
      destination: 'Novi Sad',
      startDate: new Date('2025-12-02T12:00'),
      endDate: new Date('2025-12-02T13:30'),
      cancelled: false,
      cancelledBy: null,
      price: 1800,
      panicTriggered: false
    }
  ];

  rides = new MatTableDataSource(this.ridesData);
  fromDate: Date | null = null;
  toDate: Date | null = null;
  searchText: string = '';

  ngAfterViewInit() {
    this.rides.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.searchText = filterValue;
    this.rides.filterPredicate = (data, filter) => {
      const text = (data.pickup + ' ' + data.destination).toLowerCase();
      return text.includes(filter);
    };
    this.rides.filter = filterValue.trim().toLowerCase();
  }

  applyDateFilter() {
    if (this.fromDate && this.toDate) {
      if (this.searchText) {
        this.rides.filterPredicate = (data, filter) => {
          const rideStart = new Date(data.startDate);
          const rideEnd = new Date(data.endDate);
          
          const from = new Date(this.fromDate!);
          from.setHours(0, 0, 0, 0);
          
          const to = new Date(this.toDate!);
          to.setHours(23, 59, 59, 999);
          
          const matchesDate = rideStart >= from && rideEnd <= to;
          
          const text = (data.pickup + ' ' + data.destination).toLowerCase();
          const matchesSearch = text.includes(this.searchText.toLowerCase());
          
          return matchesDate && matchesSearch;
        };
      } else {
        this.rides.filterPredicate = (data, filter) => {
          const rideStart = new Date(data.startDate);
          const rideEnd = new Date(data.endDate);
          
          const from = new Date(this.fromDate!);
          from.setHours(0, 0, 0, 0);
          
          const to = new Date(this.toDate!);
          to.setHours(23, 59, 59, 999);
          
          return rideStart >= from && rideEnd <= to;
        };
      }
      this.rides.filter = 'dateFilter';
    } else if (this.searchText) {
      this.applyFilter(this.searchText);
    } else {
      this.rides.filter = '';
    }
  }

  clearFilters() {
    this.rides.filter = '';
    
    this.fromDate = null;
    this.toDate = null;
    this.searchText = ''; 
  }
}