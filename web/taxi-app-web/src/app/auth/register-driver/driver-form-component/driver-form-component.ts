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
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password : ['', Validators.required],
      confirmPassword : ['', Validators.required]
  });
  }

  passwordsMatch(): boolean {
    return (
      this.form.get('password')?.value ===
      this.form.get('confirmPassword')?.value
    );
  }

  onNext(){
    if (this.form.valid && this.passwordsMatch()) {
      this.next.emit(this.form.value);
    }
  }

}
