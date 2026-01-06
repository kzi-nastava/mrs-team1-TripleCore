import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MapComponent } from '../../map/map';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-ride-details',
  standalone: true,
  imports: [CommonModule, MapComponent, MatButtonModule],
  templateUrl: './admin-ride-details.html',
  styleUrls: ['./admin-ride-details.css']
})
export class AdminRideDetailsComponent implements OnInit {

  ridesData = [
    {
      id: 1,
      pickup: 'Novi Sad',
      destination: 'Beograd',
      startDate: new Date('2025-12-01T10:00'),
      endDate: new Date('2025-12-01T11:15'),
      cancelled: false,
      cancelledBy: null,
      price: 2500,
      panicTriggered: false,
      driver: { name: 'Marko Markovic', vehicle: 'Audi A3', rating: 4.5 },
      passengers: [{ name: 'Ana Misic' }, { name: 'Ivana Ilic' }],
      route: [[45.253, 19.836], [45.267, 19.833]],
      reports: [{ description: 'No inconsistencies' }],
      rating: 4.5,
      status: 'Completed'
    },
    {
      id: 2,
      pickup: 'Beograd',
      destination: 'Niš',
      startDate: new Date('2025-12-03T08:00'),
      endDate: new Date('2025-12-03T08:30'),
      cancelled: true,
      cancelledBy: 'DRIVER',
      price: 0,
      panicTriggered: true,
      driver: { name: 'Jovan Jovanovic', vehicle: 'BMW 320', rating: 4.0 },
      passengers: [{ name: 'Marko Petrovic' }],
      route: [[44.7866, 20.4489], [43.3209, 21.8958]],
      reports: [{ description: 'Panic button triggered by passenger' }],
      rating: null,
      status: 'Cancelled',
      additionalInfo: 'Ride was cancelled 5 minutes after start'
    }
  ];

  rideId!: number;
  rideData: any; 

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.rideId = +this.route.snapshot.paramMap.get('id')!;
    this.rideData = this.ridesData.find(r => r.id === this.rideId); 
  }

  getPanicTriggeredBy(): string {
    if (!this.rideData?.panicTriggered) return '';
    

    return this.rideData.passengers?.[0]?.name || 'Unknown';
  }

  repeatRouteNow() {
    console.log('Repeating route now for ride:', this.rideId);
  }

  repeatRouteLater() {
    console.log('Repeating route later for ride:', this.rideId);
  }
}