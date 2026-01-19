import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { LogoutService } from '../../services/auth-service/logout-service';

@Component({
  selector: 'app-favorite-routes',
  standalone: true,
  imports: [NavbarComponent, RouterModule, CommonModule, MatTooltipModule],
  templateUrl: './favorite-routes.html',
  styleUrl: './favorite-routes.css',
})
export class FavoriteRoutesComponent {
  
  favoriteRoutes = [
    {
      startName: 'Bulevar Oslobođenja 10',
      destName: 'Futoška 25',
      stations: ['Maksima Gorkog 2', 'Jevrejska 5']
    },
    {
      startName: 'Železnička stanica',
      destName: 'Promenada',
      stations: ['Bulevar Cara Lazara']
    }
  ];

  constructor(private router: Router, private logoutService: LogoutService) {}

  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }

  orderRideFromFavorite(route: any) {

    this.router.navigate(['/passenger-home'], { state: { favoriteRoute: route } });
  }
}