import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DriverFormComponent } from '../driver-form-component/driver-form-component';
import { VehicleFormComponent } from '../vehicle-form-component/vehicle-form-component';
import { DriverRegistrationService } from '../../../services/admin-service/admin-register-driver-service';

@Component({
  selector: 'app-driver-registration-component',
  standalone: true,
  imports: [
    CommonModule,
    DriverFormComponent,
    VehicleFormComponent
  ],
  templateUrl: './driver-registration-component.html',
  styleUrl: './driver-registration-component.css'
})
export class DriverRegistrationComponent {

  step = 1;

  driverData: any = null;
  vehicleData: any = null;

  constructor(private driverService: DriverRegistrationService) {}


  onDriverNext(data: any) {
    this.driverData = data;
    this.step = 2;
  }


  onBack() {
    this.step = 1;
  }


  onRegister(vehicleData: any) {
    this.vehicleData = vehicleData;

    const payload = {
      firstName: this.driverData.firstName,
      lastName: this.driverData.lastName,
      email: this.driverData.email,
      address: this.driverData.address,
      phoneNumber: this.driverData.phone,

      
      password: null,       
      confirmPassword: null,

      vehicleModel: this.vehicleData.model,
      vehicleType: this.vehicleData.type,
      brand: this.vehicleData.brand,
      plateNum: this.vehicleData.licensePlate,
      seatNum: Number(this.vehicleData.seats),
      babySafe: this.vehicleData.babyTransport,
      petSafe: this.vehicleData.petsTransport

    
    };

      console.log('Payload being sent to backend:', payload);

  this.driverService.registerDriver(payload).subscribe({
    next: (res: any) => {
   
      const message = res?.message || 'Driver registered successfully! Activation link sent!';
      alert(message);
      this.resetForm();
    },
    error: (err: any) => {
      console.error('Full error object from backend:', err);
      console.error('Driver registration failed:', err);
    
      const errorMessage = err?.error?.message || 'Registration failed';
      alert(errorMessage);
    }
  });
  }

  private resetForm() {
    this.step = 1;
    this.driverData = null;
    this.vehicleData = null;
  }
}
