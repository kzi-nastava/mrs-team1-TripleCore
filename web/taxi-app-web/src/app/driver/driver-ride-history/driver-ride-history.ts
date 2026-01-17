import { ChangeDetectorRef, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RideHistoryTableComponent } from '../../shared/ride-history-table/ride-history-table';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { DriverService } from '../../services/driver-service';

@Component({
  selector: 'app-driver-ride-history',
  standalone: true,
  imports: [NavbarComponent, RideHistoryTableComponent, RouterModule],
  templateUrl: './driver-ride-history.html',
  styleUrls: ['./driver-ride-history.css'],
})
export class DriverRideHistoryComponent {
  // this will be loaded dynamically, for now hardcoding
  driverId: number = 1; 
  driverRideHistory: RideDetailsResponse[] = [];

  constructor(
    private driverService: DriverService,
    // to detect changes from outside Angular zone (like data from HTTP)
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadRideHistory();
  }

  private loadRideHistory(): void {
    this.driverService.getRideHistory(this.driverId).subscribe({
      next: (rides) => {
        // if rides is null or undefined, default to empty array
        this.driverRideHistory = rides ?? [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load driver ride history', err);
        this.driverRideHistory = [];
        this.cdr.detectChanges();
      }
    });
  }
}