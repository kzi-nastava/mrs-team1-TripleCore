import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfileResponse } from '../../models/user-profile-response';
import { DriverProfileResponse } from '../../models/driver-profile-response';
import { UpdateUserProfileRequest } from '../../models/update-user-profile-request';


@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  private baseUrl = 'http://localhost:8080/api/profile';

  constructor(private http: HttpClient) {}

  getDriverProfile(driverId: number): Observable<DriverProfileResponse> {
    return this.http.get<DriverProfileResponse>(`${this.baseUrl}/driver?driverId=${driverId}`);
  }

  getUserProfile(userId: number): Observable<UserProfileResponse> {
    return this.http.get<UserProfileResponse>(`${this.baseUrl}/user?userId=${userId}`);
  }

  updateUserProfile(userId: number, request: UpdateUserProfileRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}?userId=${userId}`, request);
  }

getUserRole(userId: number): Observable<{ role: string }> {
  return this.http.get<{ role: string }>(`${this.baseUrl}/role?userId=${userId}`);
}

createDriverProfileChangeRequest(driverId: number, request: UpdateUserProfileRequest) {
  return this.http.post(`${this.baseUrl}/driver/${driverId}/change-request`, request);
}

  getBlockedNote(userId: number): Observable<{ note: string }> {
    return this.http.get<{ note: string }>(`${this.baseUrl}/blocked-note?userId=${userId}`);
  }

}