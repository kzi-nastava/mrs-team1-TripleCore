import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RouteSharingService {  // Service to share route data across components
  private routeSubject = new BehaviorSubject<any>(null);  
  route$ = this.routeSubject.asObservable();

  setRoute(routeData: any) {
    this.routeSubject.next(routeData);  // Update the route data
  }

  clearRoute() {
    this.routeSubject.next(null); // Clear the route data - set to null
  }
}