import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DriverFormComponent } from '../driver-form-component/driver-form-component';
import { VehicleFormComponent } from '../vehicle-form-component/vehicle-form-component';

@Component({
  selector: 'app-driver-registration-component',
  imports: [CommonModule, DriverFormComponent, VehicleFormComponent],
  templateUrl: './driver-registration-component.html',
  styleUrl: './driver-registration-component.css',
  standalone: true
})
export class DriverRegistrationComponent {
  step = 1
  driverData: any = null;
  vehicleData: any = null;

  onDriverNext(data: any) { 
    this.driverData = data;
    this.step = 2;
  }

  onBack(){
    this.step = 1;
  }

  onRegister(vehicleData: any){
    this.vehicleData = vehicleData;

    const payload = {
      driver: this.driverData,
      vehicle: this.vehicleData
    }


    console.log("Payload for driver registration:", payload);
    alert('Driver registered successfully! Activation link sent!');
  }

}
