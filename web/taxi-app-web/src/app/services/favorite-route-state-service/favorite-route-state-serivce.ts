import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FavoriteRouteStateService {
  private favoriteRouteSource = new BehaviorSubject<any>(null);
  selectedRoute$ = this.favoriteRouteSource.asObservable();

  selectRoute(route: any) {
    this.favoriteRouteSource.next(route);
  }
}