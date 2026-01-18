import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from '../../models/login-response';

export interface LoginRequest {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private readonly API_URL = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  private saveUserData(response: LoginResponse): void {
    localStorage.setItem('userId', response.id.toString());
    localStorage.setItem('userEmail', response.email);
    localStorage.setItem('userRole', response.role);
    localStorage.setItem('userToken', response.token);
    localStorage.setItem('userFirstName', response.firstName);
    localStorage.setItem('userLastName', response.lastName);
    
    if (response.driverAvailable !== undefined) {
      localStorage.setItem('driverAvailable', response.driverAvailable.toString());
    }
  }

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.API_URL, payload).pipe(
      tap((response: LoginResponse) => {
        this.saveUserData(response);
        console.log('User data saved for logout');
      })
    );
  }
}