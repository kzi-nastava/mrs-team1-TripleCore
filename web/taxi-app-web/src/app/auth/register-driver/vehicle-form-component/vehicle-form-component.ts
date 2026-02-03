import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';

export enum VehicleType {
  STANDARD = 'STANDARD',
  LUXURY = 'LUXURY',
  VAN = 'VAN'
}


@Component({
  selector: 'app-vehicle-form-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicle-form-component.html',
  styleUrl: './vehicle-form-component.css',
  standalone: true
})
export class VehicleFormComponent {
  @Output() back = new EventEmitter<void>();
  @Output() register = new EventEmitter<any>();

    form! : FormGroup;



vehicleTypes = [
  VehicleType.STANDARD,
  VehicleType.LUXURY,
  VehicleType.VAN
];

  constructor(private fb: FormBuilder) { }

  ngOnInit(){
    this.form = this.fb.group({
    model: ['', Validators.required],
    brand: ['', Validators.required],
    type: [null, Validators.required],
    licensePlate: ['', Validators.required],
    seats: ['', [Validators.required, Validators.min(1)]],
    babyTransport: [false],
    petsTransport: [false]
  });
  }
  onRegister(){
    if (this.form.valid) {
      this.register.emit(this.form.value);
    }else {
      alert('Please fill in all required fields correctly!');
    }
  }

}
