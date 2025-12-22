import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink } from '@angular/router';

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

  defaultDriver = {
    email: 'driver@example.com',
    password: 'driver123'
  };

  constructor(private router: Router) {}

  login() {
    if (this.username === this.defaultDriver.email && this.password === this.defaultDriver.password) {
      this.router.navigate(['/driver-home']); 
    } else {
      alert('Invalid credentials');
      this.reset();
    }
  }

  reset() {
    this.username = '';
    this.password = '';
  }
}
