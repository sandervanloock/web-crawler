package be.sandervl.webcrawler.application.domain.crawler;

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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<String>> start(@PathVariable("siteId") Long siteId, @QueryParam("url") String url) throws IOException {
        return siteService.getImporterConfig(siteId)
                .map(config -> {
                    Importer importer = new Importer(config);
                    return getMetadataFromUrl(url, importer);
                })
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
