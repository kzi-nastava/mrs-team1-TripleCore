import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DriverStatusService {

  private activeSubject = new BehaviorSubject<boolean>(
    localStorage.getItem('driverAvailable') === 'true'
  );

  isActive$ = this.activeSubject.asObservable();

  setActive(status: boolean) {
    localStorage.setItem('driverAvailable', status.toString());
    this.activeSubject.next(status);
  }

  isActive(): boolean {
    return this.activeSubject.value;
  }
}
