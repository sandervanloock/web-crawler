import { Component, OnDestroy,OnInit } from '@angular/core';
import { CrawlDataService } from "../../services/crawl-data.service";
import { XmlPipe } from "../../pipes/xml.pipe";
import { Subscription } from "rxjs"; import { SiteStoreService } from "../../services/site-store.service"; import { ISite } from "../../models";

@Component({
  selector: 'app-crawl-preview',
  templateUrl: './crawl-preview.component.html',
  styleUrls: ['./crawl-preview.component.scss']
})
export class CrawlPreviewComponent implements OnInit,OnDestroy {
  url: string = "";
  config: string = "";
  previewData: string | undefined;
  loading = false;
  site?: ISite;

  private subscription?: Subscription;

  constructor(private crawlService: CrawlDataService,private siteStore: SiteStoreService) {
  }
    ngOnInit(): void {
    this.loading = true;
    this.siteStore.site$.subscribe(site => {
      this.loading= false;
          this.site = site;
        });
    }

  preview($event: MouseEvent) {
    $event.preventDefault();
    if (this.url.length && this.site) {
      this.loading = true
      this.subscription = this.crawlService.preview(this.site.id,this.url)
        .subscribe(res => {
          this.loading = false;
          this.previewData = res;
        });
    }
  }

  preventScroll($event: any) {
    $event.preventDefault();
    this.config = new XmlPipe().transform($event.target.value)
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
