import { Component, AfterViewInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';
import { RouterModule } from '@angular/router';
import { MapComponent } from '../map/map';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, RouterModule, MapComponent],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {
}
