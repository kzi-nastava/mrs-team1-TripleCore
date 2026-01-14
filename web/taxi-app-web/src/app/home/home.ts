import { Component, AfterViewInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { MapComponent } from '../map/map';
import { MatTooltipModule } from '@angular/material/tooltip';
import { VehicleLocation } from '../models/vehicle-location';
import { VehicleService } from '../services/vehicle-service';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, RouterModule, MapComponent, MatTooltipModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {
  vehicleLocations: VehicleLocation[] = [];

  private pollingSubscription!: Subscription;

  constructor(private vehicleService: VehicleService) {}

  ngOnInit(): void {
    this.startPolling();
  }

  ngOnDestroy(): void {
    // Prekidamo polling kada komponenta nestane da ne bi bilo memory leak-a
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
  }

  private startPolling(): void {
  this.pollingSubscription = interval(5000)
    .pipe(
      switchMap(() => {
        console.log('Šaljem zahtev ka backendu...');
        return this.vehicleService.getVehicleLocations();
      })
    )
    .subscribe({
      next: (locations) => {
        console.log('Stigli podaci:', locations);
        this.vehicleLocations = locations;
      },
      error: (err) => {
        console.error('Greška pri učitavanju lokacija:', err);
      }
    });
}

  
}
