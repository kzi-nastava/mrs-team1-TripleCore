import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ForgotPasswordService } from '../../services/auth-service/forgot-password-service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.html',
  styleUrls: ['./forgot-password.css']
})
export class ForgotPasswordComponent {
  email = '';
  error = '';
  isLoading = false;
  successMessage = '';

  constructor(
    private forgotPasswordService: ForgotPasswordService,
    private router: Router
  ) {}

  sendResetLink() {
    this.error = '';
    this.successMessage = '';

    if (!this.email.trim()) {
      this.error = 'Email is required';
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.error = 'Please enter a valid email address';
      return;
    }

    this.isLoading = true;
    
    this.forgotPasswordService.sendResetLink(this.email).subscribe({
      next: (response) => {
        this.isLoading = false;
        
        if (typeof response === 'string') {
          this.successMessage = response;
        } else {
          this.successMessage = 'Reset link has been sent to your email!';
        }
        
        this.email = '';

        alert(this.successMessage);
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
      },
      error: (error) => {
        this.isLoading = false;
        
        if (error.error && typeof error.error === 'string') {
          this.error = error.error;
        } else {
          this.error = 'Failed to send reset link. Please try again.';
        }
        
        alert(this.error);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}