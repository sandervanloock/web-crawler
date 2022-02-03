import { Component, OnInit } from '@angular/core';
import { CrawlDataService } from "../../services/crawl-data.service";
import { SiteStoreService } from "../../services/site-store.service";

@Component({
  selector: 'app-crawler-data-grid',
  templateUrl: './crawler-data-grid.component.html',
  styleUrls: ['./crawler-data-grid.component.scss']
})
export class CrawlerDataGridComponent implements OnInit {

  data: any[] = [];
  site = "vrtnu";

  constructor(private crawlDataService: CrawlDataService, private siteStore: SiteStoreService) {
  }

  ngOnInit(): void {
    this.search();
    this.siteStore.site$.subscribe(site => {
      this.site = site.name;
      this.search();
    });
  }

  private search() {
    this.crawlDataService.search(this.site).subscribe(res => this.data = res);
  }
}
