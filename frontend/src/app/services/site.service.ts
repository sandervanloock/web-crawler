import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

interface Page {
  page: number;
  pageSize: number;
  pageCount: number;
  total: number;
}

export interface StrapiPageWrapper<T> {
  data: StrapObjectWrapper<T>[];
  meta: {
    pagination: Page;
  }
}

export interface StrapObjectWrapper<T> {
  id: number;
  attributes: T;
}

export interface Site {
  id?: number;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class SiteService {

  constructor(private http: HttpClient) {
  }

  getSites(): Observable<StrapiPageWrapper<Site>> {
    return this.http.get<StrapiPageWrapper<Site>>("http://localhost:1337/api/sites", {
      headers: { 'Authorization': 'bearer f1d07c7fdd2075b2ddb61bcd346860219107bfe9e3352882fe66c3425a26ca7fe643fc4cdeaf250cd8920388d7ea1fd48cd5dafae5f7ca683f5291fc503de66a90d4ee1c4f77f4f01f0ce2e68dda98087914e87a5e7a1fd04c3d858bd210b94483eac6976f4d687cf95fd1d0f3a52bf9700a477242c67b248c550f0e03de4d93' }
    })
  }

}