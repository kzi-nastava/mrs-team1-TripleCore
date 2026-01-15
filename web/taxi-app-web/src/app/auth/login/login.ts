import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink } from '@angular/router';
import { LoginService} from '../../services/login-service';
import { LoginResponse } from '../../models/login-response';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, MatInputModule, MatButtonModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  username = '';
  password = '';
  loading = false;

  constructor(private router: Router, private loginService: LoginService) {}

  login() {
    if (!this.username || !this.password) {
      alert('Please enter email and password');
      return;
    }

    this.loading = true;

    this.loginService.login({ email: this.username, password: this.password })
      .subscribe({
        next: (res: LoginResponse) => {
          console.log('Login success:', res);

          localStorage.setItem('token', res.token);
          localStorage.setItem('role', res.role);
          localStorage.setItem('userId', res.id.toString());

          switch (res.role) {
            case 'DRIVER':
              this.router.navigate(['/driver-home']);
              break;
            case 'ADMIN':
              this.router.navigate(['/admin-home']);
              break;
            case 'PASSENGER':
              this.router.navigate(['/passenger-home']);
              break;
            default:
              alert('Unknown role');
          }

          this.reset();
        },
        error: (err) => {
          console.error('Login error:', err);
          alert(err.error?.message || 'Invalid credentials');
          this.reset();
        },
        complete: () => {
          this.loading = false;
        }
      });
  }

  reset() {
    this.username = '';
    this.password = '';
  }
}
