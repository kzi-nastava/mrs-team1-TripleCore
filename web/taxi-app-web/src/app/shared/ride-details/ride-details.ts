import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { MapComponent } from '../../map/map';

@Component({
  selector: 'app-ride-details',
  imports: [RouterModule, NavbarComponent, MapComponent],
  templateUrl: './ride-details.html',
  styleUrl: './ride-details.css',
})
export class RideDetailsComponent {

}
