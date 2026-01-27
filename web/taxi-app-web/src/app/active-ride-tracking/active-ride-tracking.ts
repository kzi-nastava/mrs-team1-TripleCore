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
import { RideService } from '../services/ride-service/ride-service';
import { ActivatedRoute } from '@angular/router';

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
  constructor(
    private route: ActivatedRoute,
    private rideService: RideService,
    private vehicleService: VehicleService, 
    private cdr: ChangeDetectorRef) {}
  
  ride!: RideDetailsResponse;
  rideTrackingInfo?: RideTrackingResponse;

  vehicleLocation?: LocationDTO;

  private trackingSub?: Subscription;


  ngOnInit(): void {

  const rideId = Number(this.route.snapshot.paramMap.get('rideId'));
  const role = localStorage.getItem('role');
  console.log('ActiveRideTrackingComponent initialized for rideId:', rideId, 'role:', role);
      
  this.rideService.getRideDetailsById(rideId).subscribe({
    next: rideDetails => {
      console.log('Ride details:', rideDetails);
      this.ride = rideDetails;
      console.log('Loaded ride details', this.ride);
      this.cdr.detectChanges();
    },
    error: err => {
      if (err.status === 404) {
        console.error('Ride not found');
      }
    }
  });

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