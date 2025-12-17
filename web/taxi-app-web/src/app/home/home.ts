import { Component, AfterViewInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {
}
