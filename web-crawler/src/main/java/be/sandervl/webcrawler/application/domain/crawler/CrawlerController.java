package be.sandervl.webcrawler.application.domain.crawler;

import be.sandervl.webcrawler.application.HttpCollectorFactory;
import com.norconex.collector.http.HttpCollector;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.link.Link;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Data
@NoArgsConstructor
class CrawlParams {
    private String startingUrl;
    private int amount;
    private int depth;
}

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    private final HttpCollectorFactory httpCollectorFactory;
    private final SiteService siteService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{siteId}")
    public String startWithCms(@PathVariable("siteId") Long siteId, @RequestBody(required = false) CrawlParams params) throws IOException {
        var site = siteService.getSite(siteId).orElseThrow();
        HttpCollector httpCollector = httpCollectorFactory.createFromConfigString(site.getData().getAttributes().getCrawlconfig());
        httpCollector.getCollectorConfig().getCrawlerConfigs().forEach(crawlerConfig -> {
            crawlerConfig.setMaxDocuments(params.getAmount());
            if (crawlerConfig instanceof HttpCrawlerConfig) {
                HttpCrawlerConfig httpCrawlerConfig = (HttpCrawlerConfig) crawlerConfig;
                httpCrawlerConfig.setMaxDepth(params.getDepth());
                httpCrawlerConfig.setLinkExtractors(crawlDoc -> Set.of(new Link(params.getStartingUrl())));
            }
        });
        httpCollector.getEventManager().addListener(evt -> {
            CrawlerMessage crawlerMessage = CrawlerMessage.builder()
                    .name(evt.getName())
                    .message(evt.getMessage())
                    .build();
            log.debug("[CRAWLER] - {}", crawlerMessage);
            if (crawlerMessage.shouldSend()) {
                simpMessagingTemplate.convertAndSend("/topic/" + siteId, crawlerMessage);
            }
        });
        CompletableFuture.runAsync(httpCollector::start);
        return "";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class CrawlerMessage {
        private String name;
        private String message;
        @Builder.Default
        private LocalDateTime date = LocalDateTime.now();

        boolean shouldSend() {
            return StringUtils.isNotBlank(name) || StringUtils.isNotBlank(message);
        }
    }
}
