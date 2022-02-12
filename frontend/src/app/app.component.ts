import { Component } from '@angular/core';
import { SiteStoreService } from "./services/site-store.service";
import { ISite } from "./models";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private siteStore: SiteStoreService) {
  }

  changeSite(site: ISite) {
    console.log("Change to site ID " + site.id);
    this.siteStore.selectSite(site);
  }
}
