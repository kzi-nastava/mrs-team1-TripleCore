import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VehicleLocation } from '../models/vehicle-location';
import { RideTrackingResponse } from '../models/ride-tracking-response';

@Injectable({
  providedIn: 'root'  
})

export class VehicleService {

  private readonly API_URL = 'http://localhost:8080/api/vehicles';

  constructor(private http: HttpClient) {}  

  getVehicleLocations(): Observable<VehicleLocation[]> {
    return this.http.get<VehicleLocation[]>(`${this.API_URL}/locations`);
  }

  getRideTrackingInfo(rideId: number): Observable<RideTrackingResponse> {
    return this.http.get<RideTrackingResponse>(`${this.API_URL}/active-ride/${rideId}`);
  }
}