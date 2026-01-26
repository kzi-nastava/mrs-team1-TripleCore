import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateReviewRequest } from '../../models/create-review-request';
import { ReviewDTO } from '../../models/review-dto';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private readonly apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) {}

  createReview(request: CreateReviewRequest): Observable<string> {
  return this.http.post(
    `${this.apiUrl}/create`,
    request,
    { responseType: 'text' } 
  );
  }

  getDriverReviews(driverId: number): Observable<ReviewDTO[]> {
    return this.http.get<ReviewDTO[]>(`${this.apiUrl}/driver/${driverId}`);
  }

  getPassengerReviews(passengerId: number): Observable<ReviewDTO[]> {
    return this.http.get<ReviewDTO[]>(`${this.apiUrl}/passenger/${passengerId}`);
  }
}
