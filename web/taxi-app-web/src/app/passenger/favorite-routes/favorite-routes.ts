import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NavbarComponent } from '../../shared/navbar/navbar';
import { LogoutService } from '../../services/auth-service/logout-service';
import { FavoriteRouteResponse } from '../../models/favorite-route-response';
import { PassengerService } from '../../services/passenger-service/passenger-service';
import { ChangeDetectorRef } from '@angular/core';
import { FavoriteRouteStateService } from '../../services/favorite-route-state-service/favorite-route-state-serivce';

@Component({
  selector: 'app-favorite-routes',
  standalone: true,
  imports: [NavbarComponent, RouterModule, CommonModule, MatTooltipModule],
  templateUrl: './favorite-routes.html',
  styleUrls: ['./favorite-routes.css'],
})
export class FavoriteRoutesComponent implements OnInit {
  
  favoriteRoutes: FavoriteRouteResponse[] = [];

  constructor(private router: Router, private logoutService: LogoutService, private passengerService: PassengerService,   private cdr: ChangeDetectorRef, private favoriteRouteState: FavoriteRouteStateService){}

   ngOnInit(): void {

    console.log('Loaded favorite routes...');
    const storedId = localStorage.getItem('userId');
    console.log('Retrieved userId from localStorage:', storedId);

    if (!storedId) {
      console.error('User ID not found in localStorage');
      return;
    }

  const passengerId = Number(storedId);

    this.passengerService.getFavoriteRoutes(passengerId).subscribe({
          next: routes => {
        console.log('TYPE:', typeof routes);
        console.log('IS ARRAY:', Array.isArray(routes));
        console.log('ROUTES:', routes);

        this.favoriteRoutes = routes;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('Error loading favorite routes', err);
      }
    });
  }


  onLogoutClick() {
    this.logoutService.logoutWithBackend();
  }

  orderRideFromFavorite(route: any) {
    this.favoriteRouteState.selectRoute(route); 
    this.router.navigate(['/passenger-home']); 
  }

  removeFavoriteRoute(route: FavoriteRouteResponse) {
  const storedId = localStorage.getItem('userId');
  if (!storedId) {
    console.error('User ID not found in localStorage');
    return;
  }
  const passengerId = Number(storedId);

  this.passengerService.removeFavoriteRoute(passengerId, route.id).subscribe({
    next: () => {
      this.favoriteRoutes = this.favoriteRoutes.filter(r => r.id !== route.id);
      this.cdr.detectChanges();
      console.log(`Route ${route.id} removed from favorites`);
    },
    error: err => {
      console.error('Failed to remove favorite route', err);
    }
  });
}


}