import { Component, OnInit } from '@angular/core';
import { CrawlDataService } from "../../services/crawl-data.service";
import { SiteStoreService } from "../../services/site-store.service";
import { Site } from "../../services/site.service";

@Component({
  selector: 'app-crawler-data-grid',
  templateUrl: './crawler-data-grid.component.html',
  styleUrls: ['./crawler-data-grid.component.scss']
})
export class CrawlerDataGridComponent implements OnInit {

  data: any[] = [];
  site?: Site;

  constructor(private crawlDataService: CrawlDataService, private siteStore: SiteStoreService) {
  }

  ngOnInit(): void {
    this.siteStore.site$.subscribe(site => {
      this.site = site;
      this.search();
    });
  }

  private search() {
    this.crawlDataService.search(this.site!).subscribe(res => this.data = res);
  }
}
