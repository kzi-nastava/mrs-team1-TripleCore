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
import { PassengerCancelRideDialogComponent } from '../passenger-cancel-ride-dialog/passenger-cancel-ride-dialog';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { LogoutService } from '../../services/auth-service/logout-service';
import { PassengerService } from '../../services/passenger-service/passenger-service';
import { RideService } from '../../services/ride-service/ride-service';
import { PassengerRide, adaptToPassengerRide } from '../../utils/passenger-ride-adapter';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { RideCancelRequest } from '../../models/ride-cancel-request';

@Component({
  selector: 'app-passenger-my-rides',
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
  templateUrl: './passenger-my-rides.html',
  styleUrls: ['./passenger-my-rides.css']
})
export class PassengerMyRidesComponent implements OnInit {
  
  allRides: PassengerRide[] = [];
  filteredRides: PassengerRide[] = [];
  
  displayedColumns: string[] = [
    'driver',       
    'vehicle',      
    'route',        
    'datetime',     
    'status',       
    'price',        
    'actions'       
  ];
  
  statusFilter: string = 'ALL';
  fromDate: Date | null = null;
  toDate: Date | null = null;
  
  isLoading: boolean = false;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private datePipe: DatePipe,
    private logoutService: LogoutService,
    private passengerService: PassengerService,
    private rideService: RideService
  ) {}

  ngOnInit(): void {
    this.loadRides();
  }

  private getPassengerId(): number {
    try {
      const userData = localStorage.getItem('user');
      if (userData) {
        const user = JSON.parse(userData);
        if (user.id) {
          return user.id;
        }
      }
      
      const passengerId = localStorage.getItem('userId');
      if (passengerId) {
        return parseInt(passengerId, 10);
      }
      
      console.warn('No passenger ID found in localStorage, using default ID 1');
      return 1;
    } catch (error) {
      console.error('Error getting passenger ID from localStorage:', error);
      return 1;
    }
  }

  loadRides(): void {
    this.isLoading = true;
    
    const passengerId = this.getPassengerId();
    console.log('Loading rides for passenger ID:', passengerId);
    
    this.passengerService.getRideHistory(passengerId).subscribe({
      next: (backendRides: RideDetailsResponse[]) => {
        console.log('Backend rides received:', backendRides);
        
        this.allRides = backendRides.map(ride => adaptToPassengerRide(ride));
        console.log('Converted to passenger rides:', this.allRides);
        
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
      const statusMap: {[key: string]: string[]} = {
        'ACCEPTED': ['ACCEPTED'],
        'STARTED': ['IN_PROGRESS'],
        'FINISHED': ['FINISHED'],
        'CANCELED': ['CANCELLED', 'REJECTED'],
        'UNRATED': ['FINISHED'], 
        'RATED': ['FINISHED']    
      };
      
      const targetStatuses = statusMap[this.statusFilter] || [this.statusFilter];
      
      if (this.statusFilter === 'UNRATED') {
        filtered = filtered.filter(ride => 
          targetStatuses.includes(ride.status) && !ride.isRated
        );
      } else if (this.statusFilter === 'RATED') {
        filtered = filtered.filter(ride => 
          targetStatuses.includes(ride.status) && ride.isRated
        );
      } else {
        filtered = filtered.filter(ride => targetStatuses.includes(ride.status));
      }
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

  openPassengerCancelDialog(ride: PassengerRide): void {
    if (!this.canCancelRide(ride)) {
      alert('You can only cancel assigned rides more than 10 minutes before scheduled time.');
      return;
    }
    
    const minutesUntil = this.getMinutesUntilRide(ride);
    const isLateCancellation = minutesUntil <= 10;
    
    const dialogRef = this.dialog.open(PassengerCancelRideDialogComponent, {
      width: '450px',
      data: { 
        ride: ride,
        minutesUntilRide: minutesUntil,
        isLateCancellation: isLateCancellation
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cancelRide(ride, result.notes, result.isLateCancellation);
      }
    });
  }

  cancelRide(ride: PassengerRide, notes: string, isLate: boolean): void {
    const cancelRequest: RideCancelRequest = {
      cancelerType: 'PASSENGER',
      reason: notes || 'Canceled by passenger'
    };
    
    this.rideService.cancelRide(ride.id, cancelRequest).subscribe({
      next: (response) => {
        console.log('Ride canceled successfully:', response);
        
        const rideIndex = this.allRides.findIndex(r => r.id === ride.id);
        if (rideIndex !== -1) {
          this.allRides[rideIndex].status = 'CANCELLED';
          this.applyFilters();
        }
        
        let message = `Ride #${ride.id} has been canceled.`;
        if (isLate) {
          message += ` Late cancellation fee: 200 RSD`;
        }
        alert(message);
      },
      error: (error) => {
        console.error('Error canceling ride:', error);
        alert('Failed to cancel ride. Please try again.');
      }
    });
  }

  canCancelRide(ride: PassengerRide): boolean {
    if (ride.status !== 'ACCEPTED') return false;
    
    const minutesUntil = this.getMinutesUntilRide(ride);
    return minutesUntil > 10;
  }

  private getMinutesUntilRide(ride: PassengerRide): number {
    const now = new Date();
    const rideTime = new Date(ride.date);
    const diffMs = rideTime.getTime() - now.getTime();
    return Math.floor(diffMs / (1000 * 60));
  }

  openRatingDialog(ride: PassengerRide): void {
    if (ride.status !== 'FINISHED') {
      alert('You can only rate finished rides.');
      return;
    }
    
    if (ride.isRated) {
      alert('You have already rated this ride.');
      return;
    }
    
    // TODO: Implement rating dialog
    alert(`Rating dialog for ride with ${ride.driverName} would open here.`);
  }

  rateRide(rideId: number, driverRating: number, vehicleRating: number, comment: string): void {
    console.log(`Rating ride ${rideId}: Driver ${driverRating}, Vehicle ${vehicleRating}`);
    
    const rideIndex = this.allRides.findIndex(r => r.id === rideId);
    if (rideIndex !== -1) {
      this.allRides[rideIndex].isRated = true;
      this.applyFilters();
      
      alert(`Thank you for rating ride #${rideId}!`);
    }
  }

  panicAlert(ride: PassengerRide): void {
    if (ride.status !== 'IN_PROGRESS') {
      alert('Panic alert can only be sent for rides in progress.');
      return;
    }

    const passengerId = this.getPassengerId();

    if (!passengerId) {
      alert('User not found.');
      return;
    }

    if (!confirm(
      `Send PANIC alert for ride with ${ride.driverName}?\n\n` +
      `This will immediately notify administrators.`
    )) {
      return;
    }

    this.rideService.activatePanic(ride.id, passengerId).subscribe({
      next: () => {
        alert('🚨 PANIC alert sent. Help is on the way.');
      },
      error: (err) => {
        console.error('Error sending panic alert:', err);
        alert('Failed to send panic alert.');
      }
    });
  }

  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }
}