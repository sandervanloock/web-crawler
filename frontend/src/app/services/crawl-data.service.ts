import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { ISite } from "../models";
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CrawlDataService {

  constructor(private http: HttpClient) {
  }

  search(site: ISite): Observable<any> {
    return this.http.get(`${environment.crawler.host}/data`, { params: new HttpParams({ fromObject: { siteId: site.id } }) });
  }

  preview(url: string, config: string): Observable<any> {
    const params = new HttpParams({ fromObject: { url: url } });
    return this.http.post(`${environment.consumer.host}/preview`, config, {
      params,
      headers: { 'Content-Type': 'application/xml' }
    });
  }
}
