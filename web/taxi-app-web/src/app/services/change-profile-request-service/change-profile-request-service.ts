import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DriverProfileSnapshot {
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  phone: string;
  profileImage?: string;
}

export interface DriverProfileChangeRequestDetails {
  requestId: number;
  currentProfile: DriverProfileSnapshot;
  requestedProfile: DriverProfileSnapshot;
  status: string;
  statusUpdatedAt: string | Date;
}

export interface DriverProfileChangeRequest {
  id: number;
  email: string;
  createdAt: string | Date;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChangeProfileRequestService {

  private readonly API_URL = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}


    getDriverProfileRequests(): Observable<DriverProfileChangeRequest[]> {
    return this.http.get<DriverProfileChangeRequest[]>(`${this.API_URL}/driver-profile-requests`);
    }

    getRequestDetails(id: number): Observable<DriverProfileChangeRequestDetails> {
        return this.http.get<DriverProfileChangeRequestDetails>(`${this.API_URL}/driver-profile-requests/${id}`);
    }

  approveRequest(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/driver-profile-requests/${id}/approve`, null);
  }

  rejectRequest(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/driver-profile-requests/${id}/reject`, null);
  }
}