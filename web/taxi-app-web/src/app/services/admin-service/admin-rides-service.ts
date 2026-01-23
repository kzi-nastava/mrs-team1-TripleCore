import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminRidesService {

  private baseUrl = 'http://localhost:8080/api/admin/rides';

  constructor(private http: HttpClient) {}

  getAllRides(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  getRideById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }
}
