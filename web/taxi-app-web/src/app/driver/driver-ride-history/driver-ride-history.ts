import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { RideHistoryTableComponent } from '../../shared/ride-history-table/ride-history-table';

@Component({
  selector: 'app-driver-ride-history',
  standalone: true,
  imports: [NavbarComponent, RideHistoryTableComponent, RouterModule],
  templateUrl: './driver-ride-history.html',
  styleUrls: ['./driver-ride-history.css'],
})
export class DriverRideHistoryComponent {

}
