import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RideService } from '../../services/ride-service/ride-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-start-ride',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './start-ride.html',
  styleUrl: './start-ride.css',
})
export class StartRideComponent implements OnInit {
  activeRide: any = null;

  @Output() rideStartedEvent = new EventEmitter<void>();

  constructor(private rideService: RideService, private router: Router, private cdRef: ChangeDetectorRef) {}

ngOnInit(): void {

  const driverIdString = localStorage.getItem('userId');

  if (!driverIdString) {
    console.error('Driver ID not found in localStorage');
    return;
  }

  const driverId = Number(driverIdString);



  this.rideService.getRideToStart(driverId).subscribe({
    next: ride => {

   console.log('Ride raw response:', ride);
    this.activeRide = ride;
    console.log('Active ride set:', this.activeRide);
    this.cdRef.detectChanges();



    },
    error: err => console.error(err)
  });

}

onStartRideClick() {

  if (!this.activeRide) return;

  const driverIdString = localStorage.getItem('userId');

  if (!driverIdString) {
    console.error('Driver ID not found in localStorage');
    return;
  }

  const driverId = Number(driverIdString);



  this.rideService.startRide(this.activeRide.id, driverId).subscribe({
    next: res => {
      console.log(res);
      alert('Ride started!');

      this.router.navigate(['/driver-home']);

      this.rideStartedEvent.emit();
    },
    error: err => console.error(err)
  });

}


}