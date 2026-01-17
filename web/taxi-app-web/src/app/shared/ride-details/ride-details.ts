import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { MapComponent } from '../../map/map';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { DatePipe, CommonModule } from '@angular/common';


@Component({
  selector: 'app-ride-details',
  imports: [RouterModule, NavbarComponent, MapComponent, DatePipe, CommonModule],
  templateUrl: './ride-details.html',
  styleUrl: './ride-details.css',
})
export class RideDetailsComponent implements OnInit {
  ride!: RideDetailsResponse;

  constructor(private router: Router) {}

  ngOnInit(): void {
    if (history.state?.ride) {
      this.ride = history.state.ride;
      console.log('Ride details loaded:', this.ride);
    } else {
      // fallback if no ride data is present, navigate back to ride history
      this.router.navigate(['/driver-ride-history']);
    }
  }
}
