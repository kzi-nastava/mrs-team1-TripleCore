import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DriverCancelRideDialogComponent } from '../driver-cancel-ride-dialog/driver-cancel-ride-dialog';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { LogoutService } from '../../services/auth-service/logout-service';
import { DriverStatusService } from '../../services/driver-service/driver-status-service';
import { FrontendRide, adaptToFrontendRide, } from '../../utils/driver-ride-adapter';
import { RideStatus } from '../../models/ride-details-response';
import { DriverService } from '../../services/driver-service';
import { RideService } from '../../services/ride-service/ride-service';

@Component({
  selector: 'app-driver-my-rides',
  standalone: true,
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatNativeDateModule,
    CommonModule,
    FormsModule,
    NavbarComponent,
    RouterLink
  ],
  providers: [DatePipe],
  templateUrl: './driver-my-rides.html',
  styleUrls: ['./driver-my-rides.css']
})
export class DriverMyRidesComponent implements OnInit {
  allRides: FrontendRide[] = [];
  filteredRides: FrontendRide[] = [];
  
  displayedColumns: string[] = ['passenger', 'route', 'datetime', 'status', 'price', 'actions'];

  statusFilter: RideStatus | 'ALL' = 'ALL';
  fromDate: Date | null = null;
  toDate: Date | null = null;

  isActive: boolean = true; 
  isLoading: boolean = false;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private datePipe: DatePipe,
    private logoutService: LogoutService,
    private driverStatusService: DriverStatusService,
    private driverService: DriverService,
    private rideService: RideService
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.loadRides();
      this.isActive = this.driverStatusService.isActive();
    });
  }

  private getDriverId(): number {
    try {
      const userData = localStorage.getItem('user');
      if (userData) {
        const user = JSON.parse(userData);
        if (user.id) {
          return user.id;
        }
      }
      
      const driverId = localStorage.getItem('driverId');
      if (driverId) {
        return parseInt(driverId, 10);
      }
      
      console.warn('No driver ID found in localStorage, using default ID 1');
      return 1;
    } catch (error) {
      console.error('Error getting driver ID from localStorage:', error);
      return 1;
    }
  }

  loadRides(): void {
    this.isLoading = true;
    
    const driverId = this.getDriverId();
    console.log('Loading rides for driver ID:', driverId);
    
    this.driverService.getRideHistory(driverId).subscribe({
      next: (backendRides) => {
        console.log('Backend rides received:', backendRides);
        
        this.allRides = backendRides.map(ride => adaptToFrontendRide(ride));
        console.log('Converted to frontend rides:', this.allRides);
        
        this.filteredRides = [...this.allRides];
        
        this.filteredRides.sort((a, b) => b.date.getTime() - a.date.getTime());
        
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading rides from backend:', error);
        this.isLoading = false;
        
        this.allRides = [];
        this.filteredRides = [];
        alert('Could not load rides. Please try again later.');
      }
    });
  }

  applyFilters(): void {
    let filtered = this.allRides;

    if (this.statusFilter !== 'ALL') {
      filtered = filtered.filter(ride => ride.status === this.statusFilter);
    }

    if (this.fromDate) {
      const from = new Date(this.fromDate);
      from.setHours(0, 0, 0, 0);
      filtered = filtered.filter(ride => {
        const rideDate = new Date(ride.date);
        rideDate.setHours(0, 0, 0, 0);
        return rideDate >= from;
      });
    }

    if (this.toDate) {
      const to = new Date(this.toDate);
      to.setHours(23, 59, 59, 999);
      filtered = filtered.filter(ride => {
        const rideDate = new Date(ride.date);
        rideDate.setHours(23, 59, 59, 999);
        return rideDate <= to;
      });
    }

    this.filteredRides = filtered.sort((a, b) => b.date.getTime() - a.date.getTime());
  }

  clearFilters(): void {
    this.statusFilter = 'ALL';
    this.fromDate = null;
    this.toDate = null;
    
    this.filteredRides = [...this.allRides];
    this.filteredRides.sort((a, b) => b.date.getTime() - a.date.getTime());
  }

  getStatusClass(status: string): string {
    const statusClassMap: {[key: string]: string} = {
      'REQUESTED': 'requested',
      'ACCEPTED': 'accepted',
      'REJECTED': 'canceled',
      'IN_PROGRESS': 'started',
      'CANCELLED': 'canceled',
      'FINISHED': 'finished'
    };
    return statusClassMap[status] || status.toLowerCase();
  }

  getStatusText(status: string): string {
    switch(status) {
      case 'REQUESTED': return 'Requested';
      case 'ACCEPTED': return 'Accepted';
      case 'REJECTED': return 'Rejected';
      case 'IN_PROGRESS': return 'In Progress';
      case 'CANCELLED': return 'Canceled';
      case 'FINISHED': return 'Finished';
      default: return status;
    }
  }

  openCancelDialog(ride: FrontendRide): void {
    if (ride.status !== 'ACCEPTED') {
      alert('Only accepted rides can be canceled.');
      return;
    }
    
    const dialogRef = this.dialog.open(DriverCancelRideDialogComponent, {
      width: '450px',
      data: { ride }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cancelRide(ride, result.reason, result.notes);
      }
    });
  }

  cancelRide(ride: FrontendRide, reason: string, notes: string): void {
    const rideIndex = this.allRides.findIndex(r => 
      r.passengerName === ride.passengerName && 
      r.date.getTime() === ride.date.getTime() &&
      r.pickup === ride.pickup
    );
    
    if (rideIndex !== -1) {
      this.rideService.cancelRide(ride.id, {
        cancelerType: 'DRIVER',
        reason: reason
      }).subscribe({
        next: (res) => {
          this.allRides[rideIndex].status = 'CANCELLED';
          this.allRides[rideIndex].notes = `Canceled: ${reason} - ${notes}`;
          this.applyFilters();
          alert(`Ride canceled successfully by ${res.cancelledBy}.`);
        },
        error: (err) => {
          console.error('Error canceling ride:', err);
          alert('Failed to cancel ride. Please try again.');
        }
      });
    }
  }

  startRide(ride: FrontendRide): void {
    if (ride.status !== 'ACCEPTED') {
      alert('Only accepted rides can be started.');
      return;
    }
    
    if (confirm(`Start ride with ${ride.passengerName}?`)) {
      const rideIndex = this.allRides.findIndex(r => 
        r.passengerName === ride.passengerName && 
        r.date.getTime() === ride.date.getTime() &&
        r.pickup === ride.pickup
      );
      
      if (rideIndex !== -1) {
        this.allRides[rideIndex].status = 'IN_PROGRESS';
        this.applyFilters();
        
        // TODO: Pozvati backend API za start ride
        alert(`Ride has been started. Safe driving!`);
      }
    }
  }

  finishRide(ride: FrontendRide): void {
    if (ride.status !== 'IN_PROGRESS') {
      alert('Only rides in progress can be finished.');
      return;
    }
    
    if (confirm(`Finish ride with ${ride.passengerName}?`)) {
      const rideIndex = this.allRides.findIndex(r => 
        r.passengerName === ride.passengerName && 
        r.date.getTime() === ride.date.getTime() &&
        r.pickup === ride.pickup
      );
      
      if (rideIndex !== -1) {
        this.allRides[rideIndex].status = 'FINISHED';
        this.applyFilters();
        
        // TODO: Pozvati backend API za finish ride
        alert(`Ride has been finished. Thank you!`);
      }
    }
  }

  private getCurrentUserId(): number {
    try {
      const userData = localStorage.getItem('user');
      if (userData) {
        const user = JSON.parse(userData);
        if (user.id) {
          return user.id;
        }
      }
      
      return this.getDriverId();
      
    } catch (error) {
      console.error('Error getting user ID:', error);
      return this.getDriverId(); 
    }
  }

  panicAlert(ride: FrontendRide): void {
    if (ride.status !== 'IN_PROGRESS') {
      alert('Panic alert can only be sent for rides in progress.');
      return;
    }
    
    if (confirm(`Send PANIC alert for ride with ${ride.passengerName}? This will notify administrators immediately.`)) {
      // find index of the ride in allRides
      const rideIndex = this.allRides.findIndex(r => 
        r.passengerName === ride.passengerName && 
        r.date.getTime() === ride.date.getTime() &&
        r.pickup === ride.pickup
      );
      
      if (rideIndex !== -1) {
        const userId = this.getCurrentUserId(); 
        
        // call backend API to activate panic
        this.rideService.activatePanic(ride.id, userId).subscribe({
          next: (response) => {
            console.log('Panic activated:', response);
            
            // update local ride data
            this.allRides[rideIndex].panic = true;
            this.applyFilters();
            
            alert(`🚨 PANIC ALERT ACTIVATED! Help is on the way.`);
          },
          error: (error) => {
            console.error('Error activating panic:', error);
            alert(`Failed to activate panic: ${error.error || error.message}`);
          }
        });
      }
    }
  }

  onLogoutClick(): void {
    if (this.isActive) {
      alert('You must go Inactive before logging out.');
      return;
    }
    this.logoutService.logoutWithBackend();
  }
}