import { Injectable } from '@angular/core';
import { Subject } from "rxjs";
import { Site } from "./site.service";

@Injectable({
  providedIn: 'root'
})
export class SiteStoreService {
  private site: Subject<Site> = new Subject<Site>();

  constructor() {
  }

  get site$() {
    return this.site.asObservable();
  }

  selectSite(site: Site) {
    this.site.next(site);
  }
}
