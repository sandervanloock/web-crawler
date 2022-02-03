import { Component } from '@angular/core';
import { CrawlDataService } from "../../services/crawl-data.service";
import { XmlPipe } from "../../pipes/xml.pipe";

@Component({
  selector: 'app-crawl-preview',
  templateUrl: './crawl-preview.component.html',
  styleUrls: ['./crawl-preview.component.scss']
})
export class CrawlPreviewComponent {
  url: string = "";
  config: string = "";
  previewData: string | undefined = JSON.parse(`{
  "vrtnws.img": [
    "https://images.vrt.be/vrtnws_share/2021/12/17/59dea091-5f74-11ec-b07d-02b7b76bf47f.jpg"
  ],
  "@timestamp": [
    "2022-02-03T09:40:10.286+0100"
  ],
  "vrtnws.body": [
    "Meer weten over De Afspraak op vrijdag select Video Vraag over dit nieuws? Zelf nieuws melden? Fout opgemerkt? lees ook De Afspraak op vrijdag Thomas Dermine (PS) is ambitieus: \\"Het is de missie van m’n leven om Wallonië op het niveau van Vlaanderen te brengen\\" vr 14 jan 21:32 De Afspraak op vrijdag Hoe diep zit het ongenoegen over de coronapolitiek? vr 10 dec 2021 22:12 De Afspraak op vrijdag \\"Hier wordt altijd een tapijtenmarkt georganiseerd\\": Bart De Wever over totstandkoming van coronamaatregelen vr 03 dec 2021 20:59 De Afspraak op vrijdag “Filip Dewinter is de invloedrijkste Vlaamse politicus van de voorbije 30 jaar”: Joël De Ceulaer wikt Vlaams Belang vr 26 nov 2021 21:20 Meer nieuws Op de hoogte blijven van nieuws uit jouw buurt? Ontdek onze nieuwe regiomeldingen"
  ],
  "vrtnws.url": [
    "https://www.vrt.be/vrtnws/nl/2021/12/17/is-belastingfraude-een-topprioriteit/"
  ],
  "vrtnws.title": [
    "Is belastingfraude een topprioriteit voor de politie? Minister van binnenlandse zaken Annelies Verlinden"
  ]
}`);
  loading = false;


  constructor(private crawlService: CrawlDataService) {
  }

  preview($event: MouseEvent) {
    $event.preventDefault();
    if (this.url.length && this.config.length) {
      this.loading = true
      this.crawlService.preview(this.url, this.config)
        .subscribe(res => {
          this.loading = false;
          this.previewData = res;
        })
    }
  }

  preventScroll($event: any) {
    $event.preventDefault();
    this.config = new XmlPipe().transform($event.target.value)
  }
}
