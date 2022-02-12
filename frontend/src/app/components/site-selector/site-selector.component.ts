import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { SiteService } from "../../services/site.service";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";
import { ISite } from "../../models";

@Component({
  selector: 'app-site-selector',
  templateUrl: './site-selector.component.html',
  styleUrls: ['./site-selector.component.scss']
})
export class SiteSelectorComponent implements OnInit {

  $sites = new Observable<ISite[]>();

  @Output()
  changeSiteEmitter = new EventEmitter<ISite>()

  constructor(private siteService: SiteService) {
  }

  ngOnInit(): void {
    this.$sites = this.siteService.getSites().pipe(map(res => res.data.map(it => {
      const site: ISite = { id: `${it.id}`, name: it.attributes.name }
      return site;
    })));
  }
}
