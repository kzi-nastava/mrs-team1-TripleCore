import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router'; 
import { OsmService } from '../services/osm';
import { RouteService } from '../services/estimate-route-service/route-service'; 
import { RouteSharingService } from '../services/estimate-route-service/route-sharing-service'; 

@Component({
  selector: 'app-estimate-route',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estimate-route.html',
  styleUrls: ['./estimate-route.css']
})
export class EstimateRouteComponent {

  // fields that hold user input
  startQuery = '';
  destinationQuery = '';

  // fields for search results and selected locations
  startResults: any[] = [];
  destinationResults: any[] = [];

  // selected locations
  startLocation: any = null;
  destinationLocation: any = null;

  isLoading = false; 

  constructor(
    private osmService: OsmService,
    private routeService: RouteService, 
    private routeSharingService: RouteSharingService, 
    private router: Router
  ) {}

  searchStart() {
    if (this.startQuery.length < 3) { // minimum 3 characters to search
      this.startResults = [];
      return;
    }
    this.osmService.search(this.startQuery)
      .subscribe(results => this.startResults = results);
  }

  searchDestination() {
    if (this.destinationQuery.length < 3) { 
      this.destinationResults = [];
      return;
    }
    this.osmService.search(this.destinationQuery)
      .subscribe(results => this.destinationResults = results);
  }

  selectStart(place: any) {
    this.startLocation = {
      address: place.display_name,
      lat: parseFloat(place.lat),
      lon: parseFloat(place.lon)
    };
    this.startQuery = place.display_name;
    this.startResults = [];
  }

  selectDestination(place: any) {
    this.destinationLocation = {
      address: place.display_name,
      lat: parseFloat(place.lat),
      lon: parseFloat(place.lon)
    };
    this.destinationQuery = place.display_name;
    this.destinationResults = [];
  }

  estimateRoute() {
    if (!this.startLocation || !this.destinationLocation) {
      alert('Please select both start and destination from the list');
      return;
    }

    this.isLoading = true;
    
    const request = {
      startAddress: this.startLocation.address,
      startLat: this.startLocation.lat,
      startLon: this.startLocation.lon,
      endAddress: this.destinationLocation.address,
      endLat: this.destinationLocation.lat,
      endLon: this.destinationLocation.lon
    };

    this.routeService.estimateRoute(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        
        const routeData = {
          ...response,
          startAddress: this.startLocation.address,
          endAddress: this.destinationLocation.address,
          startLat: this.startLocation.lat,
          startLon: this.startLocation.lon,
          endLat: this.destinationLocation.lat,
          endLon: this.destinationLocation.lon
        };
        
        this.routeSharingService.setRoute(routeData);
        
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error('Error estimating route:', error);
        alert('Failed to estimate route. Please try again.');
        this.isLoading = false;
      }
    });
  }

  clearForm() {
    this.startQuery = '';
    this.destinationQuery = '';
    this.startResults = [];
    this.destinationResults = [];
    this.startLocation = null;
    this.destinationLocation = null;
    this.isLoading = false;
  }
}