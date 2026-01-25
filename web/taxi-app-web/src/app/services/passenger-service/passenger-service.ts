import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RideDetailsResponse } from '../../models/ride-details-response';

@Injectable({
  providedIn: 'root'
})
export class PassengerService {
  private readonly apiUrl = 'http://localhost:8080/api/passengers';

  constructor(private http: HttpClient) {}

  getRideHistory(passengerId: number): Observable<RideDetailsResponse[]> {
    return this.http.get<RideDetailsResponse[]>(
      `${this.apiUrl}/${passengerId}/ride-history`
    );
  }

  getRideDetails(passengerId: number, rideId: number): Observable<RideDetailsResponse> {
    return this.http.get<RideDetailsResponse>(
      `${this.apiUrl}/${passengerId}/ride-history/${rideId}`
    );
  }
}