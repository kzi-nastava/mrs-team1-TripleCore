import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ForgotPasswordService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  sendResetLink(email: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/forgot-password?email=${encodeURIComponent(email)}`, 
      {}
    );
  }
}