import { Component, AfterViewInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { MapComponent } from '../map/map';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, RouterModule, MapComponent, MatTooltipModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {
  vehicleLocations = [
    { lat: 45.242, lng: 19.822, label: 'Vehicle 1' },
    { lat: 45.238, lng: 19.825, label: 'Vehicle 2' },
    { lat: 45.240, lng: 19.819, label: 'Vehicle 3' },
  ];
}
