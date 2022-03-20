import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {CrawlMessage} from "../../models/crawl-message";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {Observable} from "rxjs";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-crawl-data-log',
  templateUrl: './crawl-data-log.component.html',
  styleUrls: ['./crawl-data-log.component.scss']
})
export class CrawlDataLogComponent implements OnInit {

  @Input() messages = new Observable<CrawlMessage>();
  sortedMessages: CrawlMessage[] = [];
  dataSource = new MatTableDataSource<CrawlMessage>(this.sortedMessages);
  displayedColumns: string[] = ['name', 'message', 'date'];
  @ViewChild(MatTable) table: MatTable<CrawlMessage[]> | undefined;
  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  countPerEvent = new Map<string, number>();
  summaryData: [string, number][] = [];

  constructor() {
  }

  ngOnInit(): void {
    this.messages.subscribe((m: CrawlMessage) => {
      const newMessages = this.sortedMessages;
      newMessages.push(m);
      newMessages.sort((a: CrawlMessage, b: CrawlMessage) => this.compare(a.date, b.date, false));
      this.sortedMessages = newMessages;
      this.dataSource = new MatTableDataSource<CrawlMessage>(this.sortedMessages);
      this.table?.renderRows();
      if (this.paginator) {
        this.dataSource.paginator = this.paginator!;
      }

      const existingRecord = this.countPerEvent.get(m.name);
      if (existingRecord) {
        this.countPerEvent.set(m.name, existingRecord + 1);
      } else {
        this.countPerEvent.set(m.name, 1);
      }
      this.summaryData = [...this.countPerEvent.entries()];
    })
  }

  compare(a: number | string | Date, b: number | string | Date, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

}
