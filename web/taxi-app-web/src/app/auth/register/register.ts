import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { RegisterService } from '../../services/register-service';
import { RegisterRequest } from '../../models/register-request';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  firstName = '';
  lastName = '';
  address = '';
  phone = '';
  email = '';
  password = '';
  confirmPassword = '';
  profilePic: string | ArrayBuffer = 'icons/profile.png'; 
  selectedFile: File | null = null;
  loading = false;
  errorMessage = '';

  constructor(
    private cdr: ChangeDetectorRef,
    private registerService: RegisterService,
    private router: Router
  ) {}

  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.selectedFile = file;
    
    if (file) {
      if (!file.type.match('image.*')) {
        this.errorMessage = 'Please select an image file (JPG, PNG, etc.)';
        return;
      }
      
      if (file.size > 2 * 1024 * 1024) {
        this.errorMessage = 'Image size should be less than 2MB';
        return;
      }
      
      const reader = new FileReader();
      reader.onload = () => {
        this.profilePic = reader.result as string;
        this.cdr.detectChanges();
        this.errorMessage = ''; 
      };
      reader.readAsDataURL(file);
    }
  }

  removeProfilePicture() {
    this.profilePic = 'icons/profile.png';
    this.selectedFile = null;
    this.cdr.detectChanges();
  }

  register() {
    this.errorMessage = '';
    
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match!';
      return;
    }

    if (!this.firstName || !this.lastName || !this.address || 
        !this.phone || !this.email || !this.password) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.errorMessage = 'Please enter a valid email address.';
      return;
    }

    const phoneRegex = /^\+?[0-9]{9,15}$/;
    if (!phoneRegex.test(this.phone)) {
      this.errorMessage = 'Please enter a valid phone number (9-15 digits, optional + at start).';
      return;
    }

    if (this.password.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters long.';
      return;
    }

    this.loading = true;

    const registerData: RegisterRequest = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword,
      address: this.address,
      phoneNumber: this.phone,
      profileImage: this.getProfileImageForBackend() 
    };

    this.registerService.register(registerData).subscribe({
      next: (response) => {
        const alertMessage = `
          ✅ Registration Successful!
          
          Please check your email (${this.email}) for the activation link.
          
          Email sent to: ${this.email}
          Link expires in 24 hours
          After clicking the link, you can login
          
          For testing: Check Mailpit at http://localhost:8025
        `;
        
        alert(alertMessage);
        
        this.router.navigate(['/login'], {
          queryParams: { 
            registered: 'true', 
            email: this.email 
          }
        });
        
        this.resetForm();
      },
      error: (error) => {
        this.loading = false;
        
        if (error.status === 400) {
          if (typeof error.error === 'string') {
            this.errorMessage = error.error;
          } else if (error.error?.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'Invalid registration data. Please check your inputs.';
          }
        } else if (error.status === 409 || error.error?.includes('already exists')) {
          this.errorMessage = 'Email already registered. Please use a different email or login.';
        } else if (error.status === 0) {
          this.errorMessage = 'Cannot connect to server. Please check your connection.';
        } else {
          this.errorMessage = 'Registration failed. Please try again later.';
        }
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  private getProfileImageForBackend(): string {
    if (this.profilePic === 'icons/profile.png' || 
        (typeof this.profilePic === 'string' && 
         this.profilePic.includes('icons/profile.png'))) {
      return ''; 
    }
    
    if (typeof this.profilePic === 'string' && this.profilePic.startsWith('data:image')) {
      return this.profilePic;
    }
    
    return '';
  }

  cancel() {
    if (!this.loading && confirm('Are you sure you want to cancel registration? Any unsaved data will be lost.')) {
      this.resetForm();
      this.router.navigate(['/login']);
    }
  }

  resetForm() {
    this.firstName = '';
    this.lastName = '';
    this.address = '';
    this.phone = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
    this.profilePic = 'icons/profile.png';
    this.selectedFile = null;
    this.loading = false;
    this.errorMessage = '';
  }

  isFormValid(): boolean {
    return !!(
      this.firstName &&
      this.lastName &&
      this.address &&
      this.phone &&
      this.email &&
      this.password &&
      this.confirmPassword &&
      this.password === this.confirmPassword &&
      this.password.length >= 6
    );
  }
}