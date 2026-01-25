import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RideDetailsResponse } from '../../models/ride-details-response';

@Injectable({
  providedIn: 'root'
})
export class AdminRidesService {

  private baseUrl = 'http://localhost:8080/api/admin/rides';

  constructor(private http: HttpClient) {}

  getAllRides(): Observable<RideDetailsResponse[]> {
    return this.http.get<RideDetailsResponse[]>(this.baseUrl);
  }

  getRideById(id: number): Observable<RideDetailsResponse> {
    return this.http.get<RideDetailsResponse>(`${this.baseUrl}/${id}`);
  }
}
