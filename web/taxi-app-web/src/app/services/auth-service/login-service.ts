import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from '../../models/login-response';
import { DriverStatusService } from '../driver-service/driver-status-service';

export interface LoginRequest {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private readonly API_URL = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient, private driverStatusService: DriverStatusService) {}

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

    if (response.role === 'DRIVER') {
      localStorage.setItem('driverAvailable', 'true');
    }
  }

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.API_URL, payload).pipe(
      tap((response: LoginResponse) => {
        this.saveUserData(response);

        if (response.role === 'DRIVER') {
          localStorage.setItem('driverAvailable', 'true');
          this.driverStatusService.setActive(true);

          this.setDriverActive(response.id).subscribe({
            next: () => console.log('Driver set active on backend'),
            error: (err) => console.error('Failed to set driver active', err)
          });
        }

        console.log('User data saved for logout');
      })
    );
  }

  // call backend to set driver as active
  private setDriverActive(driverId: number): Observable<any> {
    return this.http.patch(
      `http://localhost:8080/api/drivers/${driverId}/availability?available=true`,
      {}, // prazan body
      { responseType: 'text' }
    );
  }

}