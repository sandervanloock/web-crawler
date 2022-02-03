package be.sandervl.webcrawler.application.domain.crawler;

import com.norconex.collector.core.crawler.CrawlerConfig;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.importer.Importer;
import com.norconex.importer.ImporterRequest;
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
@RequestMapping("/preview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImportPreviewController {

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<String>> start(@RequestBody String input, @QueryParam("url") String url) throws IOException {
        HttpCollectorConfig config = stringToConfig(input);
        return config.getCrawlerConfigs()
                .stream()
                .map(CrawlerConfig::getImporterConfig)
                .map(Importer::new)
                .flatMap(importer -> getMetadataFromUrl(url, importer))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
