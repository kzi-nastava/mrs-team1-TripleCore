import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PassengerService } from '../../services/passenger-service/passenger-service';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { MapComponent } from '../../map/map';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-passenger-ride-details',
  standalone: true,
  imports: [
    CommonModule, 
    MapComponent, 
    MatButtonModule, 
    MatIconModule, 
    MatTooltipModule, 
    RouterLink],
  templateUrl: './passenger-ride-details.html',
  styleUrls: ['./passenger-ride-details.css']
})
export class PassengerRideDetailsComponent implements OnInit {

  rideId!: number;
  ride?: RideDetailsResponse;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private passengerService: PassengerService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      const passengerId = params.get('passengerId');
      if (id) {
        this.rideId = Number(id);
        this.loadRideDetails();
      }
    });
  }

  private getPassengerId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? Number(userId) : 0;
  }

  loadRideDetails() {
    this.isLoading = true;
    const passengerId = this.getPassengerId();

    this.passengerService.getRideDetails(passengerId, this.rideId).subscribe({
      next: (ride: RideDetailsResponse) => {
        this.ride = JSON.parse(JSON.stringify(ride));
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading ride details:', err);
        this.isLoading = false;
      }
    });
  }

  repeatNow() {
    alert(`Ride #${this.rideId} will be repeated immediately.`);
    // ovdje ce ici logika za ponavljanje voznje
  }

  repeatLater() {
    alert(`You can schedule ride #${this.rideId} for later.`);
    // ovdje ce ici logika za zakazivanje voznje za kasnije
  }
}
