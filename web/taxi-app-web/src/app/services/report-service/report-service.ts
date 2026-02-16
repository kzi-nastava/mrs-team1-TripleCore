import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private baseUrl = 'http://localhost:8080/api/reports';

  constructor(private http: HttpClient) {}

  getDailyReport(userId: number, startDate: string, endDate: string) {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`, { params });
  }

  getSummary(userId: number, startDate: string, endDate: string) {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    return this.http.get<any>(`${this.baseUrl}/user/${userId}/summary`, { params });
  }

    getAllUsers() {
    return this.http.get<any[]>(`${this.baseUrl}/users`);
  }

  getAllUsersReport(startDate: string, endDate: string) {

  const params = new HttpParams()
    .set('startDate', startDate)
    .set('endDate', endDate);

  return this.http.get<any[]>(
    `${this.baseUrl}/all`,
    { params }
  );
}


}