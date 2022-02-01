import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CrawlPreviewComponent } from "./components/crawl-preview/crawl-preview.component";
import { CrawlerDataGridComponent } from "./components/crawler-data-grid/crawler-data-grid.component";

const routes: Routes = [
  { path: '', component: CrawlerDataGridComponent },
  { path: 'preview', component: CrawlPreviewComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
