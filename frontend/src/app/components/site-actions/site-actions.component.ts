import { Component, OnInit } from '@angular/core';
import { ISite } from "../../models";
import { SiteStoreService } from "../../services/site-store.service";

@Component({
  selector: 'app-site-actions',
  templateUrl: './site-actions.component.html',
  styleUrls: ['./site-actions.component.scss']
})
export class SiteActionsComponent implements OnInit {
  site?: ISite;

  constructor(private siteStore: SiteStoreService) { }

  ngOnInit(): void {
    this.siteStore.site$.subscribe(site => {
      this.site = site;
    });
  }

}
