import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RideCancelRequest } from '../../models/ride-cancel-request';
import { RideCancelResponse } from '../../models/ride-cancel-response';

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private readonly apiUrl = 'http://localhost:8080/api/rides';

  constructor(private http: HttpClient) {}

  cancelRide(rideId: number, request: RideCancelRequest): Observable<RideCancelResponse> {
    return this.http.post<RideCancelResponse>(`${this.apiUrl}/${rideId}/cancel`, request);
  }

  activatePanic(rideId: number, userId: number): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/${rideId}/panic?userId=${userId}`,
      {},
      { responseType: 'text' }
    );
  }
}
