import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RideCancelRequest } from '../../models/ride-cancel-request';
import { RideCancelResponse } from '../../models/ride-cancel-response';
import { RideStopRequest } from '../../models/ride-stop-request';
import { RideStopResponse } from '../../models/ride-stop-response';
import { RideTrackingResponse } from '../../models/ride-tracking-response';
import { RideDetailsResponse } from '../../models/ride-details-response';

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private readonly apiUrl = 'http://localhost:8080/api/rides';

  constructor(private http: HttpClient) {}

  cancelRide(rideId: number, request: RideCancelRequest): Observable<RideCancelResponse> {
    return this.http.post<RideCancelResponse>(`${this.apiUrl}/${rideId}/cancel`, request);
  }

  stopRide(rideId: number, request: RideStopRequest): Observable<RideStopResponse> {
    return this.http.post<RideStopResponse>(`${this.apiUrl}/${rideId}/stop`, request);
  }

  activatePanic(rideId: number, userId: number): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/${rideId}/panic?userId=${userId}`,
      {},
      { responseType: 'text' }
    );
  }

  // get ride details
  getRideDetailsById(id: number): Observable<RideDetailsResponse> {
    return this.http.get<RideDetailsResponse>(
      `${this.apiUrl}/ride-details/${id}`
    );
  }

  finishRide(rideId: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${rideId}/finish`, null, { responseType: 'text' });
  }

  orderRide(request: any, userEmail: string): Observable<any> {
  const headers = { 'X-User-Email': userEmail };  
  return this.http.post<any>(`${this.apiUrl}`, request, { headers });
  }

  startRide(rideId: number, driverId: number): Observable<string> {
  const headers = { 'X-User-Id': driverId.toString() };
  return this.http.post(`${this.apiUrl}/${rideId}/start`, null, { headers, responseType: 'text' });
  }

  getRideToStart(driverId: number): Observable<RideDetailsResponse | string> {
  return this.http.get<RideDetailsResponse | string>(`${this.apiUrl}/to-start/${driverId}`);
}


}