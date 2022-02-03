import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Site, SiteService } from "../../services/site.service";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";

@Component({
  selector: 'app-site-selector',
  templateUrl: './site-selector.component.html',
  styleUrls: ['./site-selector.component.scss']
})
export class SiteSelectorComponent implements OnInit {

  $sites = new Observable<Site[]>();

  @Output()
  changeSiteEmitter = new EventEmitter<Site>()

  constructor(private siteService: SiteService) {
  }

  ngOnInit(): void {
    this.$sites = this.siteService.getSites().pipe(map(res => res.data.map(it => {
      const site: Site = { id: it.id, name: it.attributes.name }
      return site;
    })));
  }
}
