import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MapComponent } from '../map/map';
import { RideDetailsResponse, LocationDTO } from '../models/ride-details-response';
import { MOCK_RIDE_DETAILS } from './mock-ride';

@Component({
  selector: 'app-active-ride-tracking',
  imports: [
    MatCardModule,
    MatButtonModule,
    CommonModule,
    MapComponent
  ],
  templateUrl: './active-ride-tracking.html',
  styleUrl: './active-ride-tracking.css',
})
export class ActiveRideTrackingComponent {
  @Output() close = new EventEmitter<void>();

  closeSelf(): void {
    this.close.emit();
  }

  @Input() ride: RideDetailsResponse = MOCK_RIDE_DETAILS;
  
  vehicleLocation: LocationDTO = {
  latitude: 45.2619,
  longitude: 19.8392,
  address: 'Futoška ulica 25, Novi Sad'
  };
}