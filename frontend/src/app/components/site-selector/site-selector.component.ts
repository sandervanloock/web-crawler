import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { SiteService } from "../../services/site.service";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";
import { ISite } from "../../models";
import { SiteStoreService } from "../../services/site-store.service";

@Component({
  selector: 'app-site-selector',
  templateUrl: './site-selector.component.html',
  styleUrls: ['./site-selector.component.scss']
})
export class SiteSelectorComponent implements OnInit {

  $sites = new Observable<ISite[]>();
  selection?: ISite;

  @Output()
  changeSiteEmitter = new EventEmitter<ISite>()
  private readonly LS_SITE_SELECTION = 'SELECTED_SITE';

  constructor(private siteService: SiteService, private siteStore: SiteStoreService) {
    const lsItem = localStorage.getItem(this.LS_SITE_SELECTION);
    if (lsItem) {
      const selectedSite = JSON.parse(lsItem);
      this.selection = selectedSite;
      this.siteStore.select(selectedSite)
    }
  }

  select(site: ISite) {
    this.selection = site;
    this.changeSiteEmitter.emit(site);
    localStorage.setItem(this.LS_SITE_SELECTION, JSON.stringify(site));
  }

  ngOnInit(): void {
    this.$sites = this.siteService.getSites().pipe(map(res => res.data.map(it => {
      const site: ISite = { id: `${it.id}`, name: it.attributes.name }
      return site;
    })));
  }
}
