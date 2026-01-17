import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RideDetailsResponse } from '../models/ride-details-response';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  private readonly apiUrl = 'http://localhost:8080/api/drivers';

  constructor(private http: HttpClient) {}

  getRideHistory(driverId: number): Observable<RideDetailsResponse[]> {
    return this.http.get<RideDetailsResponse[]>(
      `${this.apiUrl}/${driverId}/ride-history`
    );
  }
}
