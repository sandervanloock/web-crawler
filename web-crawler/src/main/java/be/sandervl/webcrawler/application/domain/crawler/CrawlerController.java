package be.sandervl.webcrawler.application.domain.crawler;

import be.sandervl.webcrawler.application.HttpCollectorFactory;
import com.norconex.collector.http.HttpCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class CrawlerController {

    private final HttpCollectorFactory httpCollectorFactory;

    /**
     * Start a crawl with the given XML-config as request body
     *
     * @param config
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public String start(@RequestBody String config) throws IOException {
        HttpCollector httpCollector = httpCollectorFactory.createFromConfigString(config);
        CompletableFuture.runAsync(httpCollector::start);
        return httpCollector.getId();
    }
}
