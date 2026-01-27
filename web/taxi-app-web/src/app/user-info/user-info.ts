import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserProfileService } from '../services/user-info-service/user-info-service';
import { UserProfileResponse } from '../models/user-profile-response';
import { DriverProfileResponse } from '../models/driver-profile-response';
import { UpdateUserProfileRequest } from '../models/update-user-profile-request';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-info',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './user-info.html',
  styleUrl: './user-info.css',
})
export class UserInfoComponent implements OnInit { 
  firstName = '';
  lastName = '';
  address = '';
  email = '';
  phone = '';
  profilePic: string = 'icons/profile.png'; 

    constructor(private cdr: ChangeDetectorRef, private userProfileService: UserProfileService) {}

    hasChanges: boolean = false;
    successMessage: string | null = null;

    errors: { [key: string]: string} = {};

  ngOnInit(): void {
      this.loadUserProfile();
    }

  loadUserProfile(){
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      console.error('User not logged in');
      return;
  }

    const userId = Number(userIdStr);
    this.userProfileService.getUserProfile(userId).subscribe({
      next: (response: UserProfileResponse | DriverProfileResponse) =>{
        console.log('Profile loaded:', response);
        this.firstName = response.firstName;
        this.lastName = response.lastName;
        this.address = response.address;
        this.email = response.email;
        this.phone = response.phone;
        if (response.profileImage) {
          this.profilePic = response.profileImage;
        }
        this.hasChanges = false;
        Object.keys(this.editable).forEach(key => this.editable[key] = false);
        this.cdr.detectChanges();
      },
      error: (error: any) => {
        console.error('Error loading profile:', error);
      }
    })
  }

  firstNameValid(){
    return this.firstName.length >= 2 && this.firstName.length <= 50;
  }

  lastNameValid(){
    return this.lastName.length >= 2 && this.lastName.length <= 50;
  }

  addressValid(){
    return this.address.length <= 100;
  }

  phoneValid(){
     return /^\+?[0-9]{9,15}$/.test(this.phone);
  }

  emailValid(){ ;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email);
  }

  isFormValid(){
    return this.firstNameValid() && this.lastNameValid() && this.addressValid() && this.phoneValid() && this.emailValid();
  }

    onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.profilePic = reader.result as string;
        this.hasChanges = true;
        this.cdr.detectChanges(); 
      };
      reader.readAsDataURL(file);
    }
  }

  editable: { [key: string]: boolean} = {
    firstName: false,
    lastName: false,
    address: false,
    email: false,
    phone: false
  };
   

  enableEdit(field: string){
    this.editable[field] = true;
  }

  markChanged(){
    this.hasChanges = true;
    this.successMessage = null;
  }

  resetEdit(){
    this.loadUserProfile();
    this.hasChanges = false;
    Object.keys(this.editable).forEach(key => this.editable[key] = false);
    this.successMessage = null;
    this.errors = {};
  }

submitChanges() {
  if (!this.isFormValid()) {
    console.error('Form is invalid. Please correct the errors before submitting.');
    return;
  }

  const userId = Number(localStorage.getItem('userId'));
  const updateRequest: UpdateUserProfileRequest = {
    firstName: this.firstName,
    lastName: this.lastName,
    address: this.address,
    phone: this.phone.replace(/\s+/g, ''),
    email: this.email,
    profileImage: this.profilePic
  };

  this.userProfileService.getUserRole(userId).subscribe({
    next: res => {
      const role = res.role;

      if (role === 'DRIVER') {
        this.userProfileService.createDriverProfileChangeRequest(userId, updateRequest).subscribe({
          next: () => {
            this.successMessage = 'Driver profile change request submitted successfully';
            this.hasChanges = false;
            Object.keys(this.editable).forEach(key => this.editable[key] = false);
            this.cdr.detectChanges();
          },
          error: err => {
            console.error('Error submitting driver profile change request:', err);
          }
        });
      } else {
        this.userProfileService.updateUserProfile(userId, updateRequest).subscribe({
          next: () => {
            console.log('Profile updated successfully');
            this.successMessage = 'Profile updated successfully';
            this.hasChanges = false;
            Object.keys(this.editable).forEach(key => this.editable[key] = false);
            this.cdr.detectChanges();
          },
          error: err => {
            console.error('Error updating profile:', err);
          }
        });
      }
    },
    error: err => {
      console.error('Error fetching user role:', err);
    }
  });
}


  validateField(field: string){
    switch(field){
      case 'firstName':
        if (!this.firstName.trim()) {
          this.errors['firstName'] = 'First name is required';
        } else if (this.firstName.length < 2) {
          this.errors['firstName'] = 'First name must be at least 2 characters long';
        } else if (this.firstName.length > 50) {
          this.errors['firstName'] = 'First name must be at most 50 characters long';
        } else{
          delete this.errors['firstName'];
        }
        break;
      case 'lastName':
        if (!this.lastName.trim()) {
          this.errors['lastName'] = 'Last name is required';
        } else if (this.lastName.length < 2) {
          this.errors['lastName'] = 'Last name must be at least 2 characters long';
        } else if (this.lastName.length > 50) {
          this.errors['lastName'] = 'Last name must be at most 50 characters long';
        } else{
          delete this.errors['lastName'];
        }
        break;

      case 'address':
        if (!this.address.trim()) {
          this.errors['address'] = 'Address is required';
        }else if (this.address.length > 100) {
          this.errors['address'] = 'Address must be at most 100 characters long';
        } else{
          delete this.errors['address'];
        } 
        break;
      
      case 'phone':
        if (!this.phone.trim()) {
          this.errors['phone'] = 'Phone number is required';
        } else if (!this.phoneValid()) {
          this.errors['phone'] = 'Phone number format is invalid';
        } else{
          delete this.errors['phone'];
        }
        break;

      case 'email':
        if (!this.email.trim()) {
          this.errors['email'] = 'Email is required';
        } else if (!this.emailValid()) {
          this.errors['email'] = 'Email format is invalid';
        } else{
          delete this.errors['email'];
        }
        break;

    }
  }

}