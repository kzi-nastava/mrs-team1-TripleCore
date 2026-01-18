import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResetPasswordService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  resetPassword(userId: string, newPassword: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/reset-password?userId=${userId}&newPassword=${encodeURIComponent(newPassword)}`,
      {},
      { responseType: 'text' } 
    );
  }
}