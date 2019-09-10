package be.sandervl.webcrawler.application.controllers;

import be.sandervl.webcrawler.application.HttpCollectorFactory;
import be.sandervl.webcrawler.application.config.CrawlerConstants;
import com.norconex.collector.http.HttpCollector;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
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
//        FileUtils.deleteDirectory(new File(CrawlerConstants.WORK_DIRECTORY));
        CompletableFuture.runAsync(() -> httpCollector.start(false));
        return httpCollector.getId();
    }
}
