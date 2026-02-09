import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { VehiclePricesDTO } from '../../models/vehicle-prices-dto';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-pricing',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-pricing.html',
  styleUrl: './admin-pricing.css',
})
export class AdminPricingComponent {
  prices: VehiclePricesDTO = {
  standard: 0,
  van: 0,
  luxury: 0
};

submit() {
  

}


}
