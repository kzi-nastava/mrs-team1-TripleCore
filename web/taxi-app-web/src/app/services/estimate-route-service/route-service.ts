import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RouteRequest } from '../../models/route-request';
import { RouteResponse } from '../../models/route-response';

@Injectable({
  providedIn: 'root'
})
export class RouteService {
  private apiUrl = 'http://localhost:8080/api/rides'; 

  constructor(private http: HttpClient) {}

  estimateRoute(request: RouteRequest): Observable<RouteResponse> {
    return this.http.post<RouteResponse>(`${this.apiUrl}/estimate`, request);
  }
}