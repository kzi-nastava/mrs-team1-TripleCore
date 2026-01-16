import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterResponse } from '../models/register-response';
import { RegisterRequest } from '../models/register-request';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private readonly API_URL = 'http://localhost:8080/api/auth/register';

  constructor(private http: HttpClient) {}

  register(userData: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(this.API_URL, userData);
  }
}