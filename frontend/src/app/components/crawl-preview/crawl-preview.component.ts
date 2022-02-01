import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-crawl-preview',
  templateUrl: './crawl-preview.component.html',
  styleUrls: ['./crawl-preview.component.scss']
})
export class CrawlPreviewComponent implements OnInit {
  url: string = "";
  config: string = "";

  constructor() {
  }

  ngOnInit(): void {
  }

  preview($event: MouseEvent) {
    $event.preventDefault();
    console.log("preview crawl")
  }
}
