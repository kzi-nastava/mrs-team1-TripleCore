import { Component, OnInit, OnDestroy } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { MapComponent } from '../map/map';
import { MatTooltipModule } from '@angular/material/tooltip';
import { VehicleLocation } from '../models/vehicle-location';
import { VehicleService } from '../services/vehicle-service';
import { Subscription, interval } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, RouterModule, MapComponent, MatTooltipModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  vehicleLocations: VehicleLocation[] = [];

  private pollingSubscription!: Subscription;

  constructor(private vehicleService: VehicleService) {}

  ngOnInit(): void {
    // this.startPolling();
    console.log('HomeComponent initialized (polling is disabled).');
  }

  ngOnDestroy(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
  }

  private startPolling(): void {
  this.pollingSubscription = interval(5000).pipe(
    startWith(0),
    switchMap(() => this.vehicleService.getVehicleLocations())
  ).subscribe({
    next: locations => {
      console.log('Locations from backend arrived:', locations);
      this.vehicleLocations = [...locations];
    },
    error: err => console.error(err)
  });
}
}
