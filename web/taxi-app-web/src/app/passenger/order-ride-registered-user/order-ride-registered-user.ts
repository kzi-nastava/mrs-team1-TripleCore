import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OsmService } from '../../services/osm';

@Component({
  selector: 'app-order-ride-registered-user',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-ride-registered-user.html',
  styleUrl: './order-ride-registered-user.css',
})
export class OrderRideRegisteredUser {
  @Output() rideOrderedEvent = new EventEmitter<void>();

  // Kontrola vidljivosti rezultata
  isStartFocused = false;
  isDestFocused = false;
  activeStationIndex: number | null = null;

  startPointQuery = "";
  destinationPointQuery = "";
  startPointResults: any[] = [];
  destinationPointResults: any[] = [];
  startPointLocation: any = null;
  destinationPointLocation: any = null;

  stations: any[] = [{ query: "", results: [], location: null }];
  passengersEmails: string[] = [""];
  startTime: string = "";
  babyTransport: boolean = false;
  petsTransport: boolean = false;

  constructor(private osmService: OsmService) {}


  searchStartPoint() {
    if (this.startPointQuery.length < 3) { this.startPointResults = []; return; }
    this.osmService.search(this.startPointQuery).subscribe(res => this.startPointResults = res);
  }

  searchDestinationPoint() {
    if (this.destinationPointQuery.length < 3) { this.destinationPointResults = []; return; }
    this.osmService.search(this.destinationPointQuery).subscribe(res => this.destinationPointResults = res);
  }


  hideStartResults() { setTimeout(() => this.isStartFocused = false, 200); }
  hideDestResults() { setTimeout(() => this.isDestFocused = false, 200); }
  hideStationResults() { setTimeout(() => this.activeStationIndex = null, 200); }

  selectStartPoint(place: any) {
    this.startPointLocation = { address: place.display_name, lat: place.lat, lon: place.lon };
    this.startPointQuery = place.display_name;
    this.startPointResults = [];
    this.isStartFocused = false;
  }

  selectDestinationPoint(place: any) {
    this.destinationPointLocation = { address: place.display_name, lat: place.lat, lon: place.lon };
    this.destinationPointQuery = place.display_name;
    this.destinationPointResults = [];
    this.isDestFocused = false;
  }


  searchStation(index: number) {
    const station = this.stations[index];
    if (station.query.length < 3) { station.results = []; return; }
    this.osmService.search(station.query).subscribe(res => station.results = res);
  }

  selectStation(index: number, place: any) {
    this.stations[index].location = { address: place.display_name, lat: place.lat, lon: place.lon };
    this.stations[index].query = place.display_name;
    this.stations[index].results = [];
    this.activeStationIndex = null;
  }

  addStation() { this.stations.push({ query: "", results: [], location: null }); }
  removeStation(index: number) {
    if (this.stations.length > 1) this.stations.splice(index, 1);
    else this.stations[0] = { query: "", results: [], location: null };
  }


  addPassengerEmail() { this.passengersEmails.push(""); }
  removePassengerEmail(index: number) { if (this.passengersEmails.length > 1) this.passengersEmails.splice(index, 1); }
  trackByFn(index: any, item: any) { return index; }


  private isValidEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  private isValidTime(time: string): boolean {
    return /^([01]\d|2[0-3]):([0-5]\d)$/.test(time);
  }

  orderRide() {
    if (!this.startPointLocation || !this.destinationPointLocation) {
      alert("Please select both start and destination points from the list.");
      return;
    }

    if (this.startTime && !this.isValidTime(this.startTime)) {
      alert("Invalid time! Use 24h format (e.g. 14:30).");
      return;
    }

    const filledEmails = this.passengersEmails.filter(e => e.trim() !== "");
    for (let email of filledEmails) {
      if (!this.isValidEmail(email)) {
        alert(`Email "${email}" is not valid.`);
        return;
      }
    }

    const rideOrder = {
      startPoint: this.startPointLocation,
      destinationPoint: this.destinationPointLocation,
      stations: this.stations.map(s => s.location).filter(l => l !== null),
      passengersEmails: filledEmails,
      scheduledTime: this.startTime,
      options: { babyTransport: this.babyTransport, petsTransport: this.petsTransport }
    };

    console.log("RIDE ORDER READY:", rideOrder);
    alert("Ride ordered successfully!");
    this.clearForm();
    this.rideOrderedEvent.emit();
  }

  clearForm() {
    this.startPointQuery = ""; this.destinationPointQuery = "";
    this.startPointLocation = null; this.destinationPointLocation = null;
    this.stations = [{ query: "", results: [], location: null }];
    this.passengersEmails = [""]; this.startTime = "";
    this.babyTransport = false; this.petsTransport = false;
    this.isStartFocused = false; this.isDestFocused = false;
    this.activeStationIndex = null;
  }
}