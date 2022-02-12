import { Injectable } from '@angular/core';
import { Subject } from "rxjs";
import { ISite } from "../models";

@Injectable({
  providedIn: 'root'
})
export class SiteStoreService {
  private site: Subject<ISite> = new Subject<ISite>();

  constructor() {
  }

  get site$() {
    return this.site.asObservable();
  }

  selectSite(site: ISite) {
    this.site.next(site);
  }
}
