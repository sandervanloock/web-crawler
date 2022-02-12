import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ISite, StrapiPageWrapper } from "../models";
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SiteService {

  constructor(private http: HttpClient) {
  }

  getSites(): Observable<StrapiPageWrapper<ISite>> {
    return this.http.get<StrapiPageWrapper<ISite>>(`${environment.cms.host}/api/sites`, {
      headers: { 'Authorization': `bearer ${environment.cms.token}` }
    })
  }

}
