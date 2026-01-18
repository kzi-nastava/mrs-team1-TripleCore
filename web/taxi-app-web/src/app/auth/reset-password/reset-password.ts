import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ResetPasswordService } from '../../services/auth-service/reset-password-service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.css']
})
export class ResetPasswordComponent implements OnInit {
  password: string = '';
  confirmPassword: string = '';
  userId: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private resetPasswordService: ResetPasswordService,
    private router: Router
  ) {}

  ngOnInit() {
    // get userId from URL query params
    const urlParams = new URLSearchParams(window.location.search);

    this.userId = urlParams.get('userId') || '';
    
    if (!this.userId) {
      this.errorMessage = 'Invalid reset link';
    }
  }

  resetPassword() {
    // Reset messages
    this.errorMessage = '';
    this.successMessage = '';

    // Validation
    if (!this.userId) {
      this.errorMessage = 'Invalid reset link';
      return;
    }

    if (!this.password || !this.confirmPassword) {
      this.errorMessage = 'Both password fields are required';
      return;
    }
    
    if (this.password.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters long';
      return;
    }
    
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match!';
      return;
    }

    // Send request
    this.isLoading = true;
    
    this.resetPasswordService.resetPassword(this.userId, this.password).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = 'Password reset successful! You can now login.';
        
        // Navigate to login after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);

        alert(this.successMessage);

      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error || 'Password reset failed';

        alert(this.errorMessage);
      }
    });
  }

  resetForm() {
    this.password = '';
    this.confirmPassword = '';
    this.errorMessage = '';
    this.successMessage = '';
  }
}