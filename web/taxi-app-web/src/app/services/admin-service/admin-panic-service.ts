import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PanicAlert } from '../../models/panic-alert';

@Injectable({
  providedIn: 'root'
})
export class AdminPanicService {
  private baseUrl = 'http://localhost:8080/api/admin'; 

  constructor(private http: HttpClient) {}

  getAllPanics(): Observable<PanicAlert[]> {
    return this.http.get<PanicAlert[]>(`${this.baseUrl}/panics`);
  }

  getActivePanics(): Observable<PanicAlert[]> {
    return this.http.get<PanicAlert[]>(`${this.baseUrl}/panics/active`);
  }

  resolvePanic(id: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/panics/${id}/resolve`, {});
  }
}
