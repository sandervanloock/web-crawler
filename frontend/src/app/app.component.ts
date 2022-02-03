import { Component } from '@angular/core';
import { SiteStoreService } from "./services/site-store.service";
import { Site } from "./services/site.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';

  constructor(private siteStore: SiteStoreService) {
  }

  changeSite(site: Site) {
    console.log("Change to site ID " + site.id);
    this.siteStore.selectSite(site);
  }
}
