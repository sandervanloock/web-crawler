package be.sandervl.webcrawler.application.domain.crawler.controller;

import be.sandervl.webcrawler.application.domain.crawler.HttpCollectorBuilder;
import be.sandervl.webcrawler.application.domain.site.SiteService;
import com.norconex.importer.Importer;
import com.norconex.importer.ImporterRequest;
import com.norconex.importer.response.ImporterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/preview/{siteId}")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ImportPreviewController {

    private final SiteService siteService;
    private final HttpCollectorBuilder httpCollectorBuilder;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<String>> start(@PathVariable("siteId") Long siteId, @QueryParam("url") String url) {
        CrawlerController.CrawlParams params = CrawlerController.CrawlParams.builder().startingUrl(url).amount(1).build();
        return httpCollectorBuilder.getHttpCollectorConfig(siteId, params)
                .flatMap(httpCollectorBuilder::createImporter)
                .map(importer -> getMetadataFromUrl(url, importer))
                .orElse(Collections.emptyMap());
    }

    private Map<String, List<String>> getMetadataFromUrl(String url, Importer imp) {
        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            ImporterRequest req = new ImporterRequest(inputStream);
            ImporterResponse resp = imp.importDocument(req);
            return resp.getDocument().getMetadata();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
