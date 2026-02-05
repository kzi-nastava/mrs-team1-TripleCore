import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotificationResponse } from '../models/notification-response';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly apiUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) {}

  getPassengerNotifications(passengerId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(
        `${this.apiUrl}/passenger/${passengerId}`
    )
  }

//   markNotificationSeen(notificationId: number): 
}
