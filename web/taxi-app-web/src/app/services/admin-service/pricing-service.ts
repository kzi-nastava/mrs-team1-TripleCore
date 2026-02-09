import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VehiclePricesDTO } from '../../models/vehicle-prices-dto';
import { ChangePricesRequest } from '../../models/change-prices-request';

@Injectable({
  providedIn: 'root'
})
export class PricingService {

  private readonly baseUrl = 'http://localhost:8080/api/prices';

  constructor(private http: HttpClient) {}

  getPrices(): Observable<VehiclePricesDTO> {
    return this.http.get<VehiclePricesDTO>(`${this.baseUrl}/get`);
  }

  changePrices(request: ChangePricesRequest): Observable<string> {
    return this.http.post(`${this.baseUrl}/change`, request, {
      responseType: 'text'
    });
  }
}
