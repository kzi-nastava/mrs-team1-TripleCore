import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { ReportService } from '../services/report-service/report-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-report',
  imports: [FormsModule, CommonModule],
  templateUrl: './admin-report.html',
  styleUrl: './admin-report.css',
})
export class AdminReportComponent implements OnInit {

  startDate!: string;
  endDate!: string;

  users: any[] = [];
  selectedUserId: any = "all";

  reportData: any[] = [];

  rideChart: any;
  distanceChart: any;
  priceChart: any;

  summary: any;

  constructor(private reportService: ReportService) {}

  ngOnInit() {

    this.reportService.getAllUsers()
      .subscribe(users => this.users = users);

  }

loadReport() {

  if (!this.startDate || !this.endDate) {
    alert("Select dates");
    return;
  }


  if (this.selectedUserId === "all") {

    this.reportService
      .getAllUsersReport(this.startDate, this.endDate)
      .subscribe(data => {

        this.reportData = data;

        this.createSummary(data);

        this.createCharts();

      });

  }

  else {

    this.reportService
      .getDailyReport(this.selectedUserId,
                      this.startDate,
                      this.endDate)
      .subscribe(data => {

        this.reportData = data;

        this.createSummary(data);

        this.createCharts();

      });

  }
}


   createSummary(data: any[]) {
      const totalRides = data.reduce((sum, r) => sum + Number(r.rideCount || 0), 0);
      const totalDistance = data.reduce((sum, r) => sum + Number(r.totalDistance || 0), 0);
      const totalPrice = data.reduce((sum, r) => sum + Number(r.totalPrice || 0), 0);

      const averageDistance = totalRides ? totalDistance / totalRides : 0;
      const averagePrice = totalRides ? totalPrice / totalRides : 0;

    this.summary = {
      totalRides,
      totalDistance,
      totalPrice,
      averageDistance,
      averagePrice
    };
  }

  createCharts() {

  const labels = this.reportData.map(r => r.date);
  const rides = this.reportData.map(r => Number(r.rideCount || 0));
  const distance = this.reportData.map(r => Number(r.totalDistance || 0));
  const price = this.reportData.map(r => Number(r.totalPrice || 0));

  const cumulative = (arr: number[]) => {
    let sum = 0;
    return arr.map(v => sum += v);
  };

  const average = (arr: number[]) => {
    const avg =
      arr.reduce((a,b) => a+b, 0) / arr.length;

    return arr.map(() => avg);
  };

  if (this.rideChart) this.rideChart.destroy();
  if (this.distanceChart) this.distanceChart.destroy();
  if (this.priceChart) this.priceChart.destroy();

  this.rideChart = new Chart("rideChart", {
    type: 'line',
    data: {
      labels,
      datasets: [
        { label: "Ride count", data: rides },
        { label: "Cumulative", data: cumulative(rides) },
        { label: "Average", data: average(rides) }
      ]
    }
  });

  this.distanceChart = new Chart("distanceChart", {
    type: 'line',
    data: {
      labels,
      datasets: [
        { label: "Distance (m)", data: distance },
        { label: "Cumulative", data: cumulative(distance) },
        { label: "Average", data: average(distance) }
      ]
    }
  });

  this.priceChart = new Chart("priceChart", {
    type: 'line',
    data: {
      labels,
      datasets: [
        { label: "Price (RSD)", data: price },
        { label: "Cumulative", data: cumulative(price) },
        { label: "Average", data: average(price) }
      ]
    }
  });
}


}
