import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserBlockedResponse } from '../../models/user-blocked-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminBlockService {

  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getNonAdminUsers(): Observable<UserBlockedResponse[]> {

    return this.http.get<UserBlockedResponse[]>(
      `${this.apiUrl}/users/non-admin`
    );

  }

  blockUser(userId: number, note?: string): Observable<UserBlockedResponse> {

  let url = `${this.apiUrl}/users/${userId}/block`;

  if (note) {
    url += `?note=${encodeURIComponent(note)}`;
  }

  return this.http.put<UserBlockedResponse>(url, {});
}


}
