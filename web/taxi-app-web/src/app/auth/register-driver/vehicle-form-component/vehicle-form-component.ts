import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';

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


    vehicleTypes: string[] = [
      'Standard',
      'Luxury',
      'Van'
    
  ];

  constructor(private fb: FormBuilder) { }

  ngOnInit(){
    this.form = this.fb.group({
    model: ['', Validators.required],
    type: ['', Validators.required],
    licensePlate: ['', Validators.required],
    seats: ['', Validators.required],
    babyTransport: [false],
    petsTransport: [false]
  });
  }
  onRegister(){
    if (this.form.valid) {
      this.register.emit(this.form.value);
    }
  }

}
