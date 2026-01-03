import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' }) // one instance for the whole app
export class OsmService {

  private baseUrl = 'https://nominatim.openstreetmap.org/search'; // url for nominatim search API 
  // text is given and returns list of places matching the text

  constructor(private http: HttpClient) {}

  search(query: string): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl, {
      params: {
        q: query, // search query
        format: 'json', // response format: address, lat, lon
        addressdetails: '1',
        limit: '5'
      }
    });
  }
}
