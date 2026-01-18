import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { DriverStatusService } from '../driver-service/driver-status-service';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private router: Router,
    private driverStatusService: DriverStatusService
  ) {}

  logoutWithBackend(): void {

    const role = this.getUserRole();

    // block logout if driver is active
    if (role === 'DRIVER' && this.driverStatusService.isActive()) {
      alert('You must go inactive before logging out.');
      return;
    }

    const userId = this.getUserId();
    
    if (!userId) {
      console.log('No user ID found, performing local logout');
      this.performLocalLogout();
      return;
    }

    if (!confirm('Are you sure you want to log out?')) {
      return;
    }

    console.log('Logging out user ID:', userId);

    this.http.post(
      `${this.apiUrl}/logout?userId=${userId}`,
      {},
      { responseType: 'text' }
    ).subscribe({
      next: (response) => {
        console.log('Backend logout successful:', response);
        this.performLocalLogout();
        alert('You have been logged out successfully.');
      },
      error: (error) => {
        console.error('Backend logout error:', error);
        this.handleLogoutError(error);
      }
    });
  }


  logoutLocalOnly(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.performLocalLogout();
      alert('Logged out successfully.');
    }
  }

  getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : null;
  }

  isLoggedIn(): boolean {
    return !!this.getUserId();
  }

  getUserEmail(): string {
    return localStorage.getItem('userEmail') || '';
  }

  getUserRole(): string {
    return localStorage.getItem('userRole') || '';
  }

  private performLocalLogout(): void {
    localStorage.clear();
    this.router.navigate(['/home']);
  }

  private handleLogoutError(error: any): void {
    let errorMessage = 'Logout failed. Please try again.';
    
    if (error.error && typeof error.error === 'string') {
      errorMessage = error.error;
    } else if (error.message) {
      errorMessage = error.message;
    }

    if (errorMessage.toLowerCase().includes('active ride')) {
      alert(`${errorMessage}\n\nPlease finish your ride before logging out.`);
    } else {
      alert(`${errorMessage}\n\nPerforming local logout only.`);
      this.performLocalLogout();
    }
  }
}