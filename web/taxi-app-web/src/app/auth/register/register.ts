import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
  profilePic: string = 'icons/profile.png';

  constructor(private cdr: ChangeDetectorRef) {}

  onFileSelected(event: any) {
    const file = event.target.files[0];
    console.log('Selected file:', file);
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.profilePic = reader.result as string;
        this.cdr.detectChanges(); 
      };
      reader.readAsDataURL(file);
    }
  }

  register() {
    if (this.password !== this.confirmPassword) {
      alert('Passwords do not match!');
      return;
    }

    if (!this.firstName || !this.lastName || !this.address || !this.phone || !this.email || !this.password) {
      alert('Please fill in all required fields.');
      return;
    }

    const user = {
      firstName: this.firstName,
      lastName: this.lastName,
      address: this.address,
      phone: this.phone,
      email: this.email,
      password: this.password,
      profilePic: this.profilePic
    };

    let users = JSON.parse(localStorage.getItem('users') || '[]');
    users.push(user);
    localStorage.setItem('users', JSON.stringify(users));

    console.log('User registered (stored locally):', user);
    alert('Registration successful!');

    this.resetForm();
  }

  cancel() {
    this.resetForm();
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
  }
}
