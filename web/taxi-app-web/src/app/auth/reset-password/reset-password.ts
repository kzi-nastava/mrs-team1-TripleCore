import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.css']
})
export class ResetPasswordComponent {
  password: string = '';
  confirmPassword: string = '';

  resetPassword() {
    if (this.password !== this.confirmPassword) {
      alert('Passwords do not match!');
      this.resetForm();
      return;
    }

    console.log('Password reset to:', this.password);
    alert('Password successfully reset!');

    this.resetForm();
  }

  resetForm() {
    this.password = '';
    this.confirmPassword = '';
  }
}
