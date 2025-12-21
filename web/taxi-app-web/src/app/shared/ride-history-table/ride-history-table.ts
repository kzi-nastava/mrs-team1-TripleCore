import { Component, OnInit } from '@angular/core';
import { MatTableModule } from "@angular/material/table";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';



@Component({
  selector: 'app-ride-history-table',
  imports: [MatTableModule, CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatDatepickerModule, MatNativeDateModule],
  templateUrl: './ride-history-table.html',
  styleUrl: './ride-history-table.css',
})
export class RideHistoryTableComponent implements OnInit {

  rides = [
    { pickup: '123 Main St', destination: '456 Oak St', date: '2024-01-15', start: '10:00 AM', end: '10:30 AM', price: 25.00, Panic: 'No' },
    { pickup: '789 Pine St', destination: '321 Maple St', date: '2024-01-20', start: '2:00 PM', end: '2:45 PM', price: 30.00, Panic: 'Yes' },
    { pickup: '654 Cedar St', destination: '987 Birch St', date: '2024-02-05', start: '9:00 AM', end: '9:30 AM', price: 20.00, Panic: 'No' }, 
    { pickup: '111 Elm St', destination: '222 Spruce St', date: '2024-02-10', start: '1:00 PM', end: '1:20 PM', price: 15.00, Panic: 'No' },
    { pickup: '333 Willow St', destination: '444 Ash St', date: '2024-03-01', start: '11:00 AM', end: '11:40 AM', price: 28.00, Panic: 'Yes' }
  ];

  displayedColumns: string[] = ['Pickup', 'Destination', 'Date', 'Start Time', 'End Time', 'Price', 'Panic'];

  constructor() { }

  ngOnInit(): void {
  }

}
