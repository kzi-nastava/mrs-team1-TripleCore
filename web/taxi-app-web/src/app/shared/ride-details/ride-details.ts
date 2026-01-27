import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { MapComponent } from '../../map/map';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { DatePipe, CommonModule } from '@angular/common';
import { RideService } from '../../services/ride-service/ride-service';


@Component({
  selector: 'app-ride-details',
  imports: [RouterModule, NavbarComponent, MapComponent, DatePipe, CommonModule],
  templateUrl: './ride-details.html',
  styleUrl: './ride-details.css',
})
export class RideDetailsComponent implements OnInit {
  ride!: RideDetailsResponse;

  constructor(
    private router: Router, 
    private rideService: RideService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const rideId = Number(this.route.snapshot.paramMap.get('rideId'));

    this.rideService.getRideDetailsById(rideId).subscribe({
    next: rideDetails => {
      console.log('Ride details:', rideDetails);
      this.ride = rideDetails;
      this.cdr.detectChanges();
    },
    error: err => {
      if (err.status === 404) {
        console.error('Ride not found');
      }
    }
  });
  }
}
