import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MapComponent } from '../map/map';
import { RideDetailsResponse, LocationDTO } from '../models/ride-details-response';
import { RideTrackingResponse } from '../models/ride-tracking-response';
import { VehicleService } from '../services/vehicle-service';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { RideService } from '../services/ride-service/ride-service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

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
    private location: Location,
    private route: ActivatedRoute,
    private rideService: RideService,
    private vehicleService: VehicleService, 
    private cdr: ChangeDetectorRef) {}
  
  ride!: RideDetailsResponse;
  rideTrackingInfo?: RideTrackingResponse;

  vehicleLocation?: LocationDTO;

  private trackingSub?: Subscription;
  role: string | null = null;
  rideId!: number;


  ngOnInit(): void {

  this.role = localStorage.getItem('role');
  this.rideId = Number(this.route.snapshot.paramMap.get('rideId'));
  
  console.log('ActiveRideTrackingComponent initialized for rideId:', this.rideId, 'role:', this.role);
      
  this.rideService.getRideDetailsById(this.rideId).subscribe({
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

  finishRide(): void {
    this.rideService.finishRide(this.ride.id).subscribe({
      next: (response: string) => {
        console.log('Ride finished:', response);
        alert('Ride successfully finished!');
        this.location.back();
        
      },
      error: (err) => {
        console.error('Error finishing ride:', err);
      }
    });
  }
}