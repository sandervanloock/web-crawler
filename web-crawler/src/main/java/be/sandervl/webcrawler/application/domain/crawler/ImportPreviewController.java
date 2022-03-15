package be.sandervl.webcrawler.application.domain.crawler;

import com.norconex.collector.core.crawler.CrawlerConfig;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.commons.lang.map.PropertySetter;
import com.norconex.importer.Importer;
import com.norconex.importer.ImporterRequest;
import com.norconex.importer.handler.HandlerConsumer;
import com.norconex.importer.handler.IImporterHandler;
import com.norconex.importer.handler.tagger.impl.DOMTagger;
import com.norconex.importer.response.ImporterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.sandervl.webcrawler.application.HttpCollectorFactory.stringToConfig;


@RestController
@RequestMapping("/preview/{siteId}")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImportPreviewController {

    private final SiteService siteService;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<String>> start(@PathVariable("siteId") Long siteId, @QueryParam("url") String url) throws IOException {
        var site = siteService.getSite(siteId).orElseThrow();
        SiteService.Site siteAttributes = site.getData().getAttributes();
        List<IImporterHandler> handlers =
                siteAttributes.getCrawlFields().getData().stream()
                        .map(SiteService.StrapiObjectWrapper::getAttributes)
                        .map(this::createDomTagger)
                        .collect(Collectors.toList());
        HttpCollectorConfig config = stringToConfig(siteAttributes.getCrawlconfig());
        return config.getCrawlerConfigs()
                .stream()
                .map(CrawlerConfig::getImporterConfig)
                .peek(imp -> imp.setPreParseConsumer(HandlerConsumer.fromHandlers(handlers)))
                .map(Importer::new)
                .flatMap(importer -> getMetadataFromUrl(url, importer))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private DOMTagger createDomTagger(SiteService.CrawlField crawlField) {
        DOMTagger domTagger = new DOMTagger();
        domTagger.addDOMExtractDetails(new DOMTagger.DOMExtractDetails(
                crawlField.getSelector(),
                crawlField.getName(),
                PropertySetter.REPLACE,
                crawlField.getExtract()));
        return domTagger;
    }

    private Stream<Map.Entry<String, List<String>>> getMetadataFromUrl(String url, Importer imp) {
        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            ImporterRequest req = new ImporterRequest(inputStream);
            ImporterResponse resp = imp.importDocument(req);
            return resp.getDocument().getMetadata().entrySet().stream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
