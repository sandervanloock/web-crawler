import {Component, OnInit} from '@angular/core';
import {SiteStoreService} from "../../services/site-store.service";
import {ISite} from "../../models";
import {CrawlDataService} from "../../services/crawl-data.service";
import * as SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {CrawlMessage} from "../../models/crawl-message";
import {Subject} from "rxjs";

@Component({
  selector: 'app-crawl',
  templateUrl: './crawl.component.html',
  styleUrls: ['./crawl.component.scss']
})
export class CrawlComponent implements OnInit {
  site?: ISite;
  loading = false;
  $crawlMessages = new Subject<CrawlMessage>();
  startingUrl: string | undefined;
  mntDocuments: number | undefined;

  constructor(private crawlService: CrawlDataService, private siteStore: SiteStoreService) {
  }

  ngOnInit(): void {
    this.loading = true;

    this.siteStore.site$.subscribe(site => {
      this.loading = false;
      this.site = site;
      // @ts-ignore
      const sock: SockJS = new SockJS('http://localhost:8081/websocket');
      const stompClient = Stomp.over(sock);
      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/${this.site?.id}`, (rawMessage) => {
          const message = JSON.parse(rawMessage.body) as CrawlMessage;
          this.$crawlMessages.next(message);
          this.loading = true;
        });
      });
    });

  }

  crawl(): void {
    if (this.site) {
      this.loading = true
      this.crawlService.crawl(this.site.id)
        .subscribe(res => {
          // this.loading = false;
        });
    }
  }
}
