import { Component, OnInit } from '@angular/core';
import { CrawlDataService } from "../../services/crawl-data.service";

@Component({
  selector: 'app-crawler-data-grid',
  templateUrl: './crawler-data-grid.component.html',
  styleUrls: ['./crawler-data-grid.component.scss']
})
export class CrawlerDataGridComponent implements OnInit {

  data: any[] = [];
  site = "vrtnu";

  constructor(private crawlDataService: CrawlDataService) {
  }

  ngOnInit(): void {
    this.crawlDataService.search().subscribe(res => this.data = res);
  }

}
