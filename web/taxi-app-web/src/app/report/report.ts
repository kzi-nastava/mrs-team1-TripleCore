import { Component } from '@angular/core';
import { ReportService } from '../services/report-service/report-service';
import { Chart } from 'chart.js/auto';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-report',
  imports: [FormsModule, CommonModule],
  templateUrl: './report.html',
  styleUrls: ['./report.css'],
})
export class ReportComponent {


  startDate!: string;
  endDate!: string;

  reportData: any[] = [];
  summary: any;

  userId = Number(localStorage.getItem("userId"));

  rideCountChart: any;
  priceChart: any;
  distanceChart: any;

  constructor(private reportService: ReportService) {}


  loadReport() {
    if (!this.startDate || !this.endDate) {
      alert('Please select start and end dates');
      return;
    }


    this.reportService.getDailyReport(this.userId, this.startDate, this.endDate)
      .subscribe({
        next: data => {
          this.reportData = data;
          this.createCharts(); 
        },
        error: err => console.error('Failed to load daily report', err)
      });


    this.reportService.getSummary(this.userId, this.startDate, this.endDate)
      .subscribe({
        next: data => this.summary = data,
        error: err => console.error('Failed to load summary', err)
      });
  }


  createCharts() {
    if (this.reportData.length === 0 || !this.summary) return;

    const labels = this.reportData.map(r => r.date);
    const rideCounts = this.reportData.map(r => r.rideCount);
    const prices = this.reportData.map(r => r.totalPrice);
    const distances = this.reportData.map(r => r.totalDistance);

    const cumulativeRides: number[] = [];
    const cumulativePrices: number[] = [];
    const cumulativeDistances: number[] = [];
    let sumRides = 0, sumPrices = 0, sumDistances = 0;

    rideCounts.forEach(v => { sumRides += v; cumulativeRides.push(sumRides); });
    prices.forEach(v => { sumPrices += v; cumulativePrices.push(sumPrices); });
    distances.forEach(v => { sumDistances += v; cumulativeDistances.push(sumDistances); });


    const averageRides = rideCounts.map(() => this.summary.averageRides ? this.summary.averageRides : 0);
    const averagePrices = prices.map(() => this.summary.averagePrice ? this.summary.averagePrice : 0);
    const averageDistances = distances.map(() => this.summary.averageDistance ? this.summary.averageDistance : 0);


    if (this.rideCountChart) this.rideCountChart.destroy();
    if (this.priceChart) this.priceChart.destroy();
    if (this.distanceChart) this.distanceChart.destroy();

    const rideCtx = document.getElementById('ridesCountChart') as HTMLCanvasElement;
    const priceCtx = document.getElementById('priceChart') as HTMLCanvasElement;
    const distanceCtx = document.getElementById('distanceChart') as HTMLCanvasElement;

    if (!rideCtx || !priceCtx || !distanceCtx) {
      console.error('One or more canvas elements not found');
      return;
    }


    this.rideCountChart = new Chart(rideCtx, {
      type: 'line',
      data: {
        labels,
        datasets: [
          { label: 'Ride count', data: rideCounts, borderColor: 'blue', backgroundColor: 'rgba(0,0,255,0.2)', tension: 0.3 },
          { label: 'Cumulative ride count', data: cumulativeRides, borderColor: 'orange', borderDash: [5,5], tension: 0.3 },
          { label: 'Average ride count', data: averageRides, borderColor: 'purple', borderDash: [5,5], tension: 0.3 }
        ]
      },
      options: { responsive: true, plugins: { legend: { display: true } } }
    });


    this.priceChart = new Chart(priceCtx, {
      type: 'line',
      data: {
        labels,
        datasets: [
          { label: 'Total price (RSD)', data: prices, borderColor: 'green', backgroundColor: 'rgba(0,255,0,0.2)', tension: 0.3 },
          { label: 'Cumulative price', data: cumulativePrices, borderColor: 'orange', borderDash: [5,5], tension: 0.3 },
          { label: 'Average price', data: averagePrices, borderColor: 'purple', borderDash: [5,5], tension: 0.3 }
        ]
      },
      options: { responsive: true, plugins: { legend: { display: true } } }
    });


    this.distanceChart = new Chart(distanceCtx, {
      type: 'line',
      data: {
        labels,
        datasets: [
          { label: 'Total distance (m)', data: distances, borderColor: 'red', backgroundColor: 'rgba(255,0,0,0.2)', tension: 0.3 },
          { label: 'Cumulative distance', data: cumulativeDistances, borderColor: 'orange', borderDash: [5,5], tension: 0.3 },
          { label: 'Average distance', data: averageDistances, borderColor: 'purple', borderDash: [5,5], tension: 0.3 }
        ]
      },
      options: { responsive: true, plugins: { legend: { display: true } } }
    });
  }

  
}
