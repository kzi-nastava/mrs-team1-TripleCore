import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private router: Router) {}

  logout(): void {
    if (confirm('Are you sure you want to log out?')) {
      this.router.navigate(['/home']);
    }
  }
}