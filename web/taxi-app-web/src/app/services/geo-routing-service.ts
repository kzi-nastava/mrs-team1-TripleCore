import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { LocationDTO } from '../models/ride-details-response';

@Injectable({
  providedIn: 'root'
})
export class GeoRoutingService {

  private readonly OSRM_URL = 'https://router.project-osrm.org/route/v1/driving';

  constructor(private http: HttpClient) {}

  getRoute(
    start: LocationDTO,
    stops: LocationDTO[],
    end: LocationDTO
  ): Observable<GeoJSON.LineString> {

    const points = [start, ...stops, end];

    const coords = points
      .map(p => `${p.longitude},${p.latitude}`)
      .join(';');

    const url =
      `${this.OSRM_URL}/${coords}?overview=full&geometries=geojson`;

    return this.http.get<any>(url).pipe(
      map(response => response.routes[0].geometry as GeoJSON.LineString)
    );
  }
}
