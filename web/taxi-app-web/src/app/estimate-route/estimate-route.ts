import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OsmService } from '../services/osm';

@Component({
  selector: 'app-estimate-route',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estimate-route.html',
  styleUrls: ['./estimate-route.css']
})
export class EstimateRouteComponent {

  startQuery = '';
  destinationQuery = '';

  startResults: any[] = [];
  destinationResults: any[] = [];

  startLocation: any = null;
  destinationLocation: any = null;

  constructor(private osmService: OsmService) {}

  searchStart() {
    if (this.startQuery.length < 3) {
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
      lat: place.lat,
      lon: place.lon
    };
    this.startQuery = place.display_name;
    this.startResults = [];
  }

  selectDestination(place: any) {
    this.destinationLocation = {
      address: place.display_name,
      lat: place.lat,
      lon: place.lon
    };
    this.destinationQuery = place.display_name;
    this.destinationResults = [];
  }

  estimateRoute() {
    if (!this.startLocation || !this.destinationLocation) {
      alert('Please select both start and destination from the list');
      return;
    }

    console.log('START:', this.startLocation);
    console.log('DESTINATION:', this.destinationLocation);

    alert(
      `Estimating route from:\n${this.startLocation.address}\n\nTo:\n${this.destinationLocation.address}`
    );

    this.clearForm();
  }

  clearForm() {
    this.startQuery = '';
    this.destinationQuery = '';
    this.startResults = [];
    this.destinationResults = [];
    this.startLocation = null;
    this.destinationLocation = null;
  }
}
