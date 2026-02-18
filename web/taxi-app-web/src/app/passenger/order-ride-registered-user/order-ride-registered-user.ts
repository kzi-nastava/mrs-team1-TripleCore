import { Component, Output, EventEmitter, OnChanges, SimpleChanges, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OsmService } from '../../services/osm';
import { Router } from '@angular/router';
import { OnInit } from '@angular/core';
import { FavoriteRouteStateService } from '../../services/favorite-route-state-service/favorite-route-state-serivce';
import { RideService } from '../../services/ride-service/ride-service';
import { VehicleType } from '../../models/vehicle-type';
import { RideRequest } from '../../models/ride-request';
import { UserProfileService } from '../../services/user-info-service/user-info-service';

@Component({
  selector: 'app-order-ride-registered-user',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-ride-registered-user.html',
  styleUrl: './order-ride-registered-user.css',
})
export class OrderRideRegisteredUser implements OnInit {
  @Output() rideOrderedEvent = new EventEmitter<void>();

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
  selectedVehicle: string = 'STANDARD';
  babyTransport: boolean = false;
  petsTransport: boolean = false;

  isBlocked: boolean = false;
  blockedNote: string = "";

  constructor(private osmService: OsmService, private router: Router, private rideService: RideService, private favoriteRouteState: FavoriteRouteStateService, private userProfileService: UserProfileService) {}

  ngOnInit() {
  this.loadBlockedNote();
  this.favoriteRouteState.selectedRoute$.subscribe(route => {
    if (route) {
      this.fillFormFromFavorite(route);
    }
  });
}



  goToFavorites() {
    this.router.navigate(['/favorite-routes']);
  }

  private fillFormFromFavorite(data: any) {
    this.startPointQuery = data.startAddress;
    this.destinationPointQuery = data.endAddress;

    this.startPointLocation = { address: data.startAddress, lat: data.startLat, lon: data.startLon };
    this.destinationPointLocation = { address: data.endAddress, lat: data.endLat, lon: data.endLon };

    if (data.stations && data.stations.length > 0) {
      this.stations = data.stations.map((s: any) => ({
        query: s.name,
        results: [],
        location: { address: s.name, lat: s.lat, lon: s.lon }
      }));
    } else {
      this.stations = [{ query: "", results: [], location: null }];
    }
  }

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
    if (station.query.length < 3) { 
      station.results = []; 
      return; 
    }
    this.osmService.search(station.query).subscribe(res => station.results = res);
  }

  selectStation(index: number, place: any) {
    this.stations[index].location = { address: place.display_name, lat: place.lat, lon: place.lon };
    this.stations[index].query = place.display_name;
    this.stations[index].results = [];
    this.activeStationIndex = null;
  }

  addStation() { 
    this.stations.push({ query: "", results: [], location: null }); 
  }

  removeStation(index: number) {
    if (this.stations.length > 1) 
      this.stations.splice(index, 1);
    else 
      this.stations[0] = { query: "", results: [], location: null };
  }

  addPassengerEmail() { 
    this.passengersEmails.push(""); 
  }

  removePassengerEmail(index: number) { 
    if (this.passengersEmails.length > 1) 
      this.passengersEmails.splice(index, 1); 
  }
  trackByFn(index: any) {
     return index; 
    }

orderRide() {
  if (!this.startPointLocation || !this.destinationPointLocation) {
    alert("Please select points from the list.");
    return;
  }

  const userEmail = localStorage.getItem('userEmail'); 
  if (!userEmail) {
    alert("User not logged in!");
    return;
  }

  const rideRequest: RideRequest = {
    startLocation: {
      address: this.startPointLocation.address,
      latitude: +this.startPointLocation.lat,
      longitude: +this.startPointLocation.lon
    },
    endLocation: {
      address: this.destinationPointLocation.address,
      latitude: +this.destinationPointLocation.lat,
      longitude: +this.destinationPointLocation.lon
    },
    intermediateStops: this.stations
      .map(s => s.location)
      .filter(l => l !== null)
      .map(l => ({ address: l.address, latitude: +l.lat, longitude: +l.lon })),
    linkedPassengerEmails: this.passengersEmails
      .filter(e => e.trim() !== ""),
    vehicleType: this.selectedVehicle as VehicleType,
    babyFriendly: this.babyTransport,
    petFriendly: this.petsTransport
  };

  if (this.startTime) {
     const [hours, minutes] = this.startTime.split(':').map(Number);
  const now = new Date();
  const date = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes);
      if (date < now) {
    alert("Start time cannot be in the past!");
    return; 
  }
    
  rideRequest.startTime = date.toISOString();
  }

  console.log("Ordering ride with userEmail:", userEmail, "rideRequest:", rideRequest);

  this.rideService.orderRide(rideRequest, userEmail).subscribe({
    next: res => {
      console.log("Ride ordered successfully:", res);
      alert("Ride ordered!");
      this.clearForm();
      this.rideOrderedEvent.emit();
    },
    error: err => {
  console.error("Error ordering ride:", err);

  if (err.error) {
    alert(err.error); 
  } else {
    alert("Failed to order ride. Don't have available driver or your account is blocked.");
  }

    }
  });
}


  clearForm() {
    this.startPointQuery = ""; this.destinationPointQuery = "";
    this.startPointLocation = null; this.destinationPointLocation = null;
    this.stations = [{ query: "", results: [], location: null }];
    this.passengersEmails = [""]; this.startTime = "";
    this.selectedVehicle = 'STANDARD';
    this.babyTransport = false; this.petsTransport = false;
  }

loadBlockedNote() {

  const userId = Number(localStorage.getItem('userId'));
  if (!userId) return;

  this.userProfileService.getBlockedNote(userId)
    .subscribe({
      next: res => {

        if (res.note != "") {
        this.isBlocked = true;
        this.blockedNote = res.note || "";

        console.log("User is blocked. Note:", this.blockedNote);
        }
    

      },

      error: err => {

        this.isBlocked = false;
        this.blockedNote = "";

        console.log("User is NOT blocked");
      }
    });
}


}