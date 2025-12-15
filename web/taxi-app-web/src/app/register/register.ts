import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
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

  register() {
    console.log('Register clicked');
    console.log({
      firstName: this.firstName,
      lastName: this.lastName,
      address: this.address,
      phone: this.phone,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    });
  }

  cancel() {
    console.log('Cancel clicked');
  }
}
