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
import { AuthService } from '../../services/auth-service/logout-service';

interface Ride {
  id: number;
  passengerName: string;
  passengerImage: string;
  passengerRating: number;
  pickup: string;
  destination: string;
  date: Date;
  scheduledTime: string;
  estimatedEnd: string;
  duration: number;
  price: number;
  status: 'ASSIGNED' | 'STARTED' | 'FINISHED' | 'CANCELED';
  panic: boolean;
  vehicleType: string;
  notes: string;
}

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
  
  allRides: Ride[] = [
    {
      id: 12345,
      passengerName: 'Ana Anić',
      passengerImage: 'assets/avatars/avatar1.jpg',
      passengerRating: 4.8,
      pickup: 'Bulevar kralja Aleksandra 123',
      destination: 'Nikola Tesla Airport',
      date: new Date(),
      scheduledTime: '14:30',
      estimatedEnd: '15:15',
      duration: 45,
      price: 1250,
      status: 'ASSIGNED',
      panic: false,
      vehicleType: 'STANDARD',
      notes: '2 suitcases'
    },
    {
      id: 12346,
      passengerName: 'Marko Marković',
      passengerImage: 'assets/avatars/avatar2.jpg',
      passengerRating: 4.5,
      pickup: 'Kneza Mihaila 45',
      destination: 'Ada Mall',
      date: new Date(Date.now() - 86400000), // yesterday
      scheduledTime: '10:00',
      estimatedEnd: '10:25',
      duration: 25,
      price: 650,
      status: 'STARTED',
      panic: false,
      vehicleType: 'LUXURY',
      notes: ''
    },
    {
      id: 12347,
      passengerName: 'Ivana Ivić',
      passengerImage: '',
      passengerRating: 4.9,
      pickup: 'Trg Republike',
      destination: 'Novi Beograd, Blok 45',
      date: new Date(Date.now() - 172800000), // 2 days ago
      scheduledTime: '16:45',
      estimatedEnd: '17:10',
      duration: 25,
      price: 580,
      status: 'FINISHED',
      panic: false,
      vehicleType: 'STANDARD',
      notes: 'Pet friendly needed'
    },
    {
      id: 12348,
      passengerName: 'Petar Petrović',
      passengerImage: 'assets/avatars/avatar3.jpg',
      passengerRating: 3.8,
      pickup: 'Zemun, Kej oslobođenja',
      destination: 'Voždovac, Kumodraška',
      date: new Date(Date.now() - 259200000), // 3 days ago
      scheduledTime: '08:30',
      estimatedEnd: '09:00',
      duration: 30,
      price: 720,
      status: 'CANCELED',
      panic: true,
      vehicleType: 'VAN',
      notes: 'Cancelled by driver'
    },
    {
      id: 12349,
      passengerName: 'Jelena Jelenić',
      passengerImage: 'assets/avatars/avatar4.jpg',
      passengerRating: 4.7,
      pickup: 'Banovo brdo',
      destination: 'Slavija Square',
      date: new Date(Date.now() + 86400000), // tomorrow
      scheduledTime: '19:00',
      estimatedEnd: '19:35',
      duration: 35,
      price: 890,
      status: 'ASSIGNED',
      panic: false,
      vehicleType: 'STANDARD',
      notes: 'Baby seat needed'
    }
  ];

  filteredRides: Ride[] = [];
  displayedColumns: string[] = ['id', 'passenger', 'route', 'datetime', 'status', 'price', 'actions'];
  
  // Filter variables
  statusFilter: string = 'ALL';
  fromDate: Date | null = null;
  toDate: Date | null = null;
  
  constructor(
    private dialog: MatDialog,
    private router: Router,
    private datePipe: DatePipe,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredRides = this.allRides.filter(ride => {
      // Filter by status
      if (this.statusFilter !== 'ALL' && ride.status !== this.statusFilter) {
        return false;
      }
      
      // Filter by date range
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
    
    // Sort by date (newest first)
    this.filteredRides.sort((a, b) => b.date.getTime() - a.date.getTime());
  }

  clearFilters(): void {
    this.statusFilter = 'ALL';
    this.fromDate = null;
    this.toDate = null;
    this.filteredRides = [...this.allRides];
    this.filteredRides.sort((a, b) => b.date.getTime() - a.date.getTime());
  }

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

  openCancelDialog(ride: Ride): void {
    const dialogRef = this.dialog.open(DriverCancelRideDialogComponent, {
      width: '450px',
      data: { ride: ride }
    });

    // subscribe function - after dialog is closed we get the result
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cancelRide(ride.id, result.reason, result.notes);
      }
    });
  }

  cancelRide(rideId: number, reason: string, notes: string): void {
    console.log(`Canceling ride ${rideId} as driver: ${reason} - ${notes}`);
    
    // Update local data
    const rideIndex = this.allRides.findIndex(r => r.id === rideId);
    if (rideIndex !== -1) {
      this.allRides[rideIndex].status = 'CANCELED';
      this.allRides[rideIndex].notes = `Canceled: ${reason} - ${notes}`;
      this.applyFilters();
      
      // Show confirmation
      alert(`Ride #${rideId} has been canceled. Passenger has been notified.`);
    }
  }

  startRide(ride: Ride): void {
    if (confirm(`Start ride #${ride.id} with ${ride.passengerName}?`)) {
      
      const rideIndex = this.allRides.findIndex(r => r.id === ride.id);
      if (rideIndex !== -1) {
        this.allRides[rideIndex].status = 'STARTED';
        this.applyFilters();
        
        alert(`Ride #${ride.id} has been started. Safe driving!`);
      }
    }
  }

  finishRide(ride: Ride): void {
    if (confirm(`Finish ride #${ride.id}?`)) {
      
      const rideIndex = this.allRides.findIndex(r => r.id === ride.id);
      if (rideIndex !== -1) {
        this.allRides[rideIndex].status = 'FINISHED';
        this.applyFilters();
        
        alert(`Ride #${ride.id} has been finished. Thank you!`);
      }
    }
  }

  panicAlert(ride: Ride): void {
    if (confirm(`Send PANIC alert for ride #${ride.id}?\n\nThis will notify administrators immediately.`)) {
      
      const rideIndex = this.allRides.findIndex(r => r.id === ride.id);
      if (rideIndex !== -1) {
        this.allRides[rideIndex].panic = true;
        this.applyFilters();
        
        alert(`PANIC alert sent for ride #${ride.id}. Help is on the way.`);
      }
    }
  }

  onLogoutClick() {
    this.authService.logout();
  }
    
}