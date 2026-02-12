import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RideDetailsResponse } from '../../models/ride-details-response';
import { FavoriteRouteResponse } from '../../models/favorite-route-response';

@Injectable({
  providedIn: 'root'
})
export class PassengerService {
  private readonly apiUrl = 'http://localhost:8080/api/passengers';

  constructor(private http: HttpClient) {}

  getRideHistory(passengerId: number): Observable<RideDetailsResponse[]> {
    return this.http.get<RideDetailsResponse[]>(
      `${this.apiUrl}/${passengerId}/ride-history`
    );
  }

  getRideDetails(passengerId: number, rideId: number): Observable<RideDetailsResponse> {
    return this.http.get<RideDetailsResponse>(
      `${this.apiUrl}/${passengerId}/ride-history/${rideId}`
    );
  }

  getFavoriteRoutes(passengerId: number): Observable<FavoriteRouteResponse[]> {
    return this.http.get<FavoriteRouteResponse[]>(
      `${this.apiUrl}/${passengerId}/favorite-routes`
    );
  }

  addToFavorites(passengerId: number, rideId: number) {
    return this.http.post(
      `${this.apiUrl}/${passengerId}/favorite-routes/${rideId}`,
      {}, { responseType: 'text' }
    );
  }

  removeFavoriteRoute(passengerId: number, routeId: number) {
    return this.http.delete(`${this.apiUrl}/${passengerId}/favorite-routes/${routeId}`, {responseType: 'text'});
  }

}