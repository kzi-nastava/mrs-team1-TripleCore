import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, Input, input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MapComponent } from '../map/map';
import { RideDetailsResponse, LocationDTO } from '../models/ride-details-response';
import { MOCK_RIDE_DETAILS } from './mock-ride';
import { RideTrackingResponse } from '../models/ride-tracking-response';
import { VehicleService } from '../services/vehicle-service';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';

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

  constructor(private vehicleService: VehicleService, private cdr: ChangeDetectorRef) {}

  @Input() ride: RideDetailsResponse = MOCK_RIDE_DETAILS;
  
  rideTrackingInfo!: RideTrackingResponse;

  vehicleLocation?: LocationDTO;

  private trackingSub?: Subscription;


  ngOnInit(): void {
  this.trackingSub = interval(2000)
    .pipe(
      switchMap(() =>
        this.vehicleService.getRideTrackingInfo(this.ride.id)
      )
    )
    .subscribe({
      next: (response) => {
        this.rideTrackingInfo = response;
        this.vehicleLocation = {
          latitude: response.vehicleLocation.latitude,
          longitude: response.vehicleLocation.longitude,
          address: response.vehicleLocation.address
        };
        console.log('Loaded ride tracking info', response);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading ride tracking info', err);
      }
    });
}

ngOnDestroy(): void {
  this.trackingSub?.unsubscribe();
}
  

}