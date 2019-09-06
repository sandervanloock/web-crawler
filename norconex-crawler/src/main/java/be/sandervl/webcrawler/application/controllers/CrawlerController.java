package be.sandervl.webcrawler.application.controllers;

import be.sandervl.webcrawler.application.CollectorConfigurationFactory;
import com.norconex.collector.http.HttpCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class CrawlerController {

    private final CollectorConfigurationFactory collectorConfigurationFactory;

    /**
     * Start a crawl with the given XML-config as request body
     *
     * @param config
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public String start(@RequestBody String config) throws IOException {
        HttpCollector httpCollector = new HttpCollector(collectorConfigurationFactory.createFromString(config));
        httpCollector.start(false);
        //httpCollector.getJobSuite().getJobStatus()
        return httpCollector.getId();
    }
}
