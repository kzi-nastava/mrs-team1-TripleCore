import { Component, AfterViewInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {
}
