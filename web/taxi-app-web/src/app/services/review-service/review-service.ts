import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateReviewRequest } from '../../models/create-review-request';

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
}
