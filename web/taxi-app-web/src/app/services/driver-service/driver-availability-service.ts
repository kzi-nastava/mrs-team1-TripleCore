import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AvailabilityResponse {
  driverId: number;
  available: boolean;
  hasActiveRide: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class DriverAvailabilityService {
  private apiUrl = 'http://localhost:8080/api/drivers';

  constructor(private http: HttpClient) {}

  changeAvailability(driverId: number, available: boolean): Observable<string> {
    return this.http.patch(
      `${this.apiUrl}/${driverId}/availability?available=${available}`,
      {},
      { responseType: 'text' }
    );
  }

  getAvailability(driverId: number): Observable<AvailabilityResponse> {
    return this.http.get<AvailabilityResponse>(`${this.apiUrl}/${driverId}/availability`);
  }
}