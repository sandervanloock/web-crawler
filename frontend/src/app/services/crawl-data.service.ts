import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Site } from "./site.service";

@Injectable({
  providedIn: 'root'
})
export class CrawlDataService {

  constructor(private http: HttpClient) {
  }

  search(site: Site): Observable<any> {
    return this.http.get("http://localhost:8082/data", { params: new HttpParams({ fromObject: { siteId: site.id } }) });
  }

  preview(url: string, config: string): Observable<any> {
    const params = new HttpParams({ fromObject: { url: url } });
    return this.http.post("http://localhost:8081/preview", config, {
      params,
      headers: { 'Content-Type': 'application/xml' }
    });
  }
}
