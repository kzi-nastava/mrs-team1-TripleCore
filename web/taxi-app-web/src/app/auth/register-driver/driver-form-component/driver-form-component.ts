import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { AbstractControl, Form, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-driver-form-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './driver-form-component.html',
  styleUrl: './driver-form-component.css',
})
export class DriverFormComponent {
  @Output() next = new EventEmitter<any>();

  form! : FormGroup;
  
  constructor(private fb: FormBuilder) { }


  ngOnInit(){
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(/\+?[0-9]{10,15}/)]],
      email: ['', [Validators.required, Validators.email]],
  });
  }


  onNext(){
    if (this.form.valid) {
      this.next.emit(this.form.value);
    } else {
      alert('Please fill in all required fields correctly!');
    }
  }

}
