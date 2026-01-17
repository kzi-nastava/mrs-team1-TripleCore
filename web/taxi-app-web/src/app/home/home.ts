import { Component, OnInit, OnDestroy } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { MapComponent } from '../map/map';
import { MatTooltipModule } from '@angular/material/tooltip';
import { VehicleLocation } from '../models/vehicle-location';
import { VehicleService } from '../services/vehicle-service';
import { Subscription, interval } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { RouteSharingService } from '../services/estimate-route-service/route-sharing-service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, RouterModule, MapComponent, MatTooltipModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  vehicleLocations: VehicleLocation[] = [];
  routeData: any = null;  

  private pollingSubscription!: Subscription;
  private routeSubscription!: Subscription; 

  constructor(
    private vehicleService: VehicleService,
    private routeSharingService: RouteSharingService
  ) {}

  ngOnInit(): void {
    // this.startPolling();
    
    this.subscribeToRouteChanges();
  }

  ngOnDestroy(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe(); 
    }
  }

  private startPolling(): void {
    this.pollingSubscription = interval(5000).pipe(
      startWith(0),
      switchMap(() => this.vehicleService.getVehicleLocations())
    ).subscribe({
      next: locations => {
        this.vehicleLocations = [...locations];
      },
      error: err => console.error(err)
    });
  }

  private subscribeToRouteChanges(): void {
    this.routeSubscription = this.routeSharingService.route$.subscribe(route => {
      this.routeData = route;
    });
  }

  clearRoute(): void {
    this.routeData = null;
    this.routeSharingService.clearRoute();
  }
}