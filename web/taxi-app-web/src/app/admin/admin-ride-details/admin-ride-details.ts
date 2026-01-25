import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminRidesService } from '../../services/admin-service/admin-rides-service';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { MapComponent } from '../../map/map';

@Component({
  selector: 'app-admin-ride-details',
  standalone: true,
  imports: [CommonModule, MapComponent],
  templateUrl: './admin-ride-details.html',
  styleUrls: ['./admin-ride-details.css']
})
export class AdminRideDetailsComponent implements OnInit {

  rideId!: number;
  ride?: RideDetailsResponse;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private adminRidesService: AdminRidesService,
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.rideId = Number(id);
        console.log('Loading ride details for ID:', this.rideId);
        this.loadRideDetails();
      }
    });
  }

  loadRideDetails() {
    this.isLoading = true;
    
    this.adminRidesService.getRideById(this.rideId).subscribe({
      next: (ride: RideDetailsResponse) => {
        
        this.ride = this.deepCopy(ride);
        
        this.isLoading = false;
        
        this.cdr.detectChanges(); // force render
        
      },
      error: (err: any) => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private deepCopy(obj: any): any {
    return JSON.parse(JSON.stringify(obj));
  }

  getOrderer(): string {
    return this.ride?.ordererName ?? '-';
  }

  getPassengers(): string[] {
    return this.ride?.linkedPassengers ?? [];
  }

  getPanicTriggeredBy(): string {
    return this.ride?.panicTriggeredBy ?? '-';
  }
}