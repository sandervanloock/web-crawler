import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatSelectModule } from "@angular/material/select";
import { HttpClientModule } from "@angular/common/http";
import { SiteSelectorComponent } from "./components/site-selector/site-selector.component";
import { CrawlerDataGridComponent } from './components/crawler-data-grid/crawler-data-grid.component';
import { MatCardModule } from "@angular/material/card";
import { AbbreviatePipe } from './pipes/abbreviate.pipe';
import { FlexLayoutModule } from "@angular/flex-layout";
import { CrawlPreviewComponent } from './components/crawl-preview/crawl-preview.component';
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [
    AppComponent,
    SiteSelectorComponent,
    CrawlerDataGridComponent,
    AbbreviatePipe,
    CrawlPreviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSelectModule,
    MatCardModule,
    HttpClientModule,
    FlexLayoutModule,
    MatInputModule,
    MatButtonModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
