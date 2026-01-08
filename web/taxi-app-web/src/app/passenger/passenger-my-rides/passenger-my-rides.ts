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

export interface PassengerRide {
  id: number;
  driverName: string;
  driverImage: string;
  driverRating: number;
  vehicleModel: string;
  vehicleType: string;
  licensePlate: string;
  babyFriendly: boolean;
  petFriendly: boolean;
  pickup: string;
  destination: string;
  date: Date;
  scheduledTime: string;
  estimatedEnd: string;
  duration: number;
  price: number;
  status: 'ASSIGNED' | 'STARTED' | 'FINISHED' | 'CANCELED';
  isRated: boolean;
  panicActivated: boolean;
  cancellationFee?: number;
}

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
  
  allRides: PassengerRide[] = [
    {
      id: 1001,
      driverName: 'Marko Marković',
      driverImage: 'assets/avatars/driver1.jpg',
      driverRating: 4.8,
      vehicleModel: 'Audi A4',
      vehicleType: 'LUXURY',
      licensePlate: 'BG-123-AB',
      babyFriendly: true,
      petFriendly: false,
      pickup: 'Trg Republike',
      destination: 'Nikola Tesla Airport',
      date: new Date(Date.now() + 30 * 60 * 1000), // 30 minutes from now
      scheduledTime: '14:30',
      estimatedEnd: '15:15',
      duration: 45,
      price: 1250,
      status: 'ASSIGNED',
      isRated: false,
      panicActivated: false
    },
    {
      id: 1002,
      driverName: 'Ivan Ivanović',
      driverImage: 'assets/avatars/driver2.jpg',
      driverRating: 4.5,
      vehicleModel: 'Toyota Corolla',
      vehicleType: 'STANDARD',
      licensePlate: 'NS-456-CD',
      babyFriendly: false,
      petFriendly: true,
      pickup: 'Kneza Mihaila',
      destination: 'Ada Mall',
      date: new Date(Date.now() - 300000), // 5 minutes ago
      scheduledTime: '10:00',
      estimatedEnd: '10:25',
      duration: 25,
      price: 650,
      status: 'STARTED',
      isRated: false,
      panicActivated: false
    },
     {
      id: 1002,
      driverName: 'Ivan Ivanović',
      driverImage: 'assets/avatars/driver2.jpg',
      driverRating: 4.5,
      vehicleModel: 'Toyota Corolla',
      vehicleType: 'STANDARD',
      licensePlate: 'NS-456-CD',
      babyFriendly: false,
      petFriendly: true,
      pickup: 'Kneza Mihaila',
      destination: 'Ada Mall',
      date: new Date(Date.now() - 300000), // 5 minutes ago
      scheduledTime: '10:00',
      estimatedEnd: '10:25',
      duration: 25,
      price: 650,
      status: 'STARTED',
      isRated: false,
      panicActivated: false
    },
    {
      id: 1010,
      driverName: 'Bojana Bojanić',
      driverImage: '',
      driverRating: 4.9,
      vehicleModel: 'BMW X5',
      vehicleType: 'LUXURY',
      licensePlate: 'BG-789-EF',
      babyFriendly: false,
      petFriendly: false,
      pickup: 'Kalemegdan',
      destination: 'Sava Center',
      date: new Date(Date.now() + 2 * 60 * 1000), // 2 minutes from now
      scheduledTime: '16:45',
      estimatedEnd: '17:15',
      duration: 30,
      price: 980,
      status: 'ASSIGNED',
      isRated: false,
      panicActivated: false
    },
    {
      id: 1004,
      driverName: 'Nikola Nikolić',
      driverImage: 'assets/avatars/driver3.jpg',
      driverRating: 4.2,
      vehicleModel: 'Mercedes Vito',
      vehicleType: 'VAN',
      licensePlate: 'NS-321-GH',
      babyFriendly: true,
      petFriendly: false,
      pickup: 'Voždovac',
      destination: 'Banovo Brdo',
      date: new Date(Date.now() - 259200000), // 3 days ago
      scheduledTime: '08:30',
      estimatedEnd: '09:00',
      duration: 30,
      price: 720,
      status: 'FINISHED',
      isRated: true,
      panicActivated: false
    },
    {
      id: 1005,
      driverName: 'Dragan Dragić',
      driverImage: '',
      driverRating: 3.8,
      vehicleModel: 'Ford Focus',
      vehicleType: 'STANDARD',
      licensePlate: 'BG-654-IJ',
      babyFriendly: false,
      petFriendly: false,
      pickup: 'Slavija',
      destination: 'Avala Tower',
      date: new Date(Date.now() - 345600000), // 4 days ago
      scheduledTime: '13:00',
      estimatedEnd: '13:45',
      duration: 45,
      price: 890,
      status: 'CANCELED',
      isRated: false,
      panicActivated: false,
      cancellationFee: 200
    }
  ];

  filteredRides: PassengerRide[] = [];
  
  displayedColumns: string[] = [
    'id',           
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
  
  constructor(
    private dialog: MatDialog,
    private router: Router,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredRides = this.allRides.filter(ride => {
      // Filter by status
      if (this.statusFilter !== 'ALL') {
        switch (this.statusFilter) {
          case 'ASSIGNED':
          case 'STARTED':
          case 'FINISHED':
          case 'CANCELED':
            if (ride.status !== this.statusFilter) {
              return false;
            }
            break;
          case 'UNRATED':
            if (!(ride.status === 'FINISHED' && !ride.isRated)) {
              return false;
            }
            break;
          case 'RATED':
            if (!(ride.status === 'FINISHED' && ride.isRated)) {
              return false;
            }
            break;
        }
      }
      
      if (this.fromDate) {
        const fromDate = new Date(this.fromDate);
        fromDate.setHours(0, 0, 0, 0);
        const rideDate = new Date(ride.date);
        rideDate.setHours(0, 0, 0, 0);
        
        if (rideDate < fromDate) {
          return false;
        }
      }
      
      if (this.toDate) {
        const toDate = new Date(this.toDate);
        toDate.setHours(23, 59, 59, 999);
        const rideDate = new Date(ride.date);
        rideDate.setHours(23, 59, 59, 999);
        
        if (rideDate > toDate) {
          return false;
        }
      }
      
      return true;
    });
    
    this.filteredRides.sort((a, b) => b.date.getTime() - a.date.getTime());
  }

  clearFilters(): void {
    this.statusFilter = 'ALL';
    this.fromDate = null;
    this.toDate = null;
    this.filteredRides = [...this.allRides];
    this.filteredRides.sort((a, b) => b.date.getTime() - a.date.getTime());
  }

  // Get CSS class for status badge
  getStatusClass(status: string): string {
    return status.toLowerCase();
  }

  getStatusText(status: string): string {
    switch(status) {
      case 'ASSIGNED': return 'Assigned';
      case 'STARTED': return 'In Progress';
      case 'FINISHED': return 'Finished';
      case 'CANCELED': return 'Canceled';
      default: return status;
    }
  }

  openPassengerCancelDialog(ride: PassengerRide): void {
    const minutesUntil = this.getMinutesUntilRide(ride);
    console.log(`Opening cancel dialog for ride ${ride.id}`);
    console.log(`Minutes until: ${minutesUntil}`);
    
    const dialogRef = this.dialog.open(PassengerCancelRideDialogComponent, {
      width: '450px',
      data: { 
        ride: ride,
        minutesUntilRide: minutesUntil
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Dialog result:', result);
      if (result) {
        this.cancelRide(ride.id, result.notes, result.isLateCancellation);
      }
    });
  }

  cancelRide(rideId: number, notes: string, isLate: boolean): void {
    console.log(`Canceling ride ${rideId}, late: ${isLate}, notes: ${notes}`);
    
    const rideIndex = this.allRides.findIndex(r => r.id === rideId);
    if (rideIndex !== -1) {
      this.allRides[rideIndex].status = 'CANCELED';
      
      if (isLate) {
        this.allRides[rideIndex].cancellationFee = 200; 
        alert(`Ride #${rideId} canceled. Late cancellation fee: 200 RSD`);
      } else {
        alert(`Ride #${rideId} canceled successfully.`);
      }
      
      this.applyFilters();
    }
  }

  canCancelRide(ride: PassengerRide): boolean {
    if (ride.status !== 'ASSIGNED') return false;
    
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

    alert('Rating dialog would open here');
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
    if (confirm(`Send PANIC alert for ride #${ride.id}?\n\nThis will notify administrators immediately.`)) {
      
      const rideIndex = this.allRides.findIndex(r => r.id === ride.id);
      if (rideIndex !== -1) {
        this.allRides[rideIndex].panicActivated = true;
        this.applyFilters();
        
        alert(`PANIC alert sent for ride #${rideIndex}. Help is on the way. Stay calm.`);
      }
    }
  }

  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/login']);
    }
  }
}