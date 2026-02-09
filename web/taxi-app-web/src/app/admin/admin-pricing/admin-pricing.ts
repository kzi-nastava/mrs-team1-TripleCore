import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VehiclePricesDTO } from '../../models/vehicle-prices-dto';
import { PricingService } from '../../services/admin-service/pricing-service';


@Component({
  selector: 'app-admin-pricing',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-pricing.html',
  styleUrl: './admin-pricing.css',
})
export class AdminPricingComponent implements OnInit {

  adminId = Number(localStorage.getItem('userId'));

  prices: VehiclePricesDTO = {
    standard: 0,
    van: 0,
    luxury: 0
  };

  private originalPrices!: VehiclePricesDTO;

  constructor(
    private pricingService: PricingService,
    private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.pricingService.getPrices().subscribe({
      next: (prices) => {
        this.prices = { ...prices };
        this.originalPrices = { ...prices };
        console.log(prices)
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load prices', err);
        alert('Failed to load prices.');
      }
    });
  }

  resetForm(): void {
    if (this.originalPrices) {
      this.prices = { ...this.originalPrices };
    }
  }

  isFormValid(): boolean {
    return (
      this.prices.standard > 0 &&
      this.prices.van > 0 &&
      this.prices.luxury > 0
    );
  }

  submitForm(): void {
    if (!this.isFormValid()) {
      alert('All prices must be greater than zero.');
      return;
    }

    this.pricingService.changePrices({
      adminId: this.adminId,
      prices: this.prices
    }).subscribe({
      next: (message) => {
        alert(message);
        this.originalPrices = { ...this.prices };
      },
      error: (err) => {
        console.error('Failed to change prices', err);
        alert(err.error || 'Failed to change prices.');
      }
    });
  }
}
