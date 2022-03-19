import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {CrawlMessage} from "../../models/crawl-message";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {Observable} from "rxjs";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-crawl-data-log',
  templateUrl: './crawl-data-log.component.html',
  styleUrls: ['./crawl-data-log.component.scss']
})
export class CrawlDataLogComponent implements OnInit, AfterViewInit {

  @Input() messages = new Observable<CrawlMessage>();
  sortedMessages: CrawlMessage[] = [];
  dataSource = new MatTableDataSource<CrawlMessage>(this.sortedMessages);
  displayedColumns: string[] = ['message', 'date'];
  @ViewChild(MatTable) table!: MatTable<CrawlMessage[]>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
  }

  ngOnInit(): void {
    this.messages.subscribe((m: CrawlMessage) => {
      const newMessages = this.sortedMessages;
      newMessages.push(m);
      newMessages.sort((a: CrawlMessage, b: CrawlMessage) => this.compare(a.date, b.date, false));
      console.log(`[LOG COMPONENT]`, newMessages);
      this.sortedMessages = newMessages;
      this.dataSource = new MatTableDataSource<CrawlMessage>(this.sortedMessages);
      this.table.renderRows();
      this.dataSource.paginator = this.paginator;
    })
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  compare(a: number | string | Date, b: number | string | Date, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

}
