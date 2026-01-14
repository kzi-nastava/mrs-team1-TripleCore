import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VehicleLocation } from '../models/vehicle-location';

@Injectable({
  providedIn: 'root'  
})

export class VehicleService {

  private readonly API_URL = 'http://localhost:8080/api/vehicles/locations';

  constructor(private http: HttpClient) {}  

  getVehicleLocations(): Observable<VehicleLocation[]> {
    return this.http.get<VehicleLocation[]>(this.API_URL);
  }
}
