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
import { FormsModule } from "@angular/forms";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XmlPipe } from './pipes/xml.pipe';
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { SiteActionsComponent } from './components/site-actions/site-actions.component';
import { CdkTableModule } from "@angular/cdk/table";
import { CrawlComponent } from './components/crawl/crawl.component';

@NgModule({
  declarations: [
    AppComponent,
    SiteSelectorComponent,
    CrawlerDataGridComponent,
    AbbreviatePipe,
    CrawlPreviewComponent,
    XmlPipe,
    SiteActionsComponent,
    CrawlComponent
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
    FormsModule,
    MatProgressSpinnerModule,
    MatMenuModule,
    MatIconModule,
    CdkTableModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
