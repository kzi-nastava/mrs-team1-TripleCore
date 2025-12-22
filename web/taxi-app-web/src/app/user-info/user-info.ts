import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { first } from 'rxjs';

@Component({
  selector: 'app-user-info',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-info.html',
  styleUrl: './user-info.css',
})
export class UserInfoComponent { 
  firstName = 'Boban';
  lastName = 'Rajovic';
  address = 'Beograd, Srbija';
  email = 'driver@example.com';
  phone = '+381 64 1234567';
  profilePic: string = 'icons/profile.png'; 

    constructor(private cdr: ChangeDetectorRef) {}

    hasChanges: boolean = false;

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
  }

  resetEdit(){
    this.firstName = 'Boban';
    this.lastName = 'Rajovic';
    this.address = 'Beograd, Srbija';
    this.email = 'driver@example.com';
    this.phone = '+381 64 1234567';
    this.profilePic = 'icons/profile.png';
    this.hasChanges = false;
    Object.keys(this.editable).forEach(key => this.editable[key] = false);
  }

  submitChanges(){
    alert('Changes submitted successfully!');
    this.hasChanges = false;
    Object.keys(this.editable).forEach(key => this.editable[key] = false);
  }
}