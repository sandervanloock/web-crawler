package be.sandervl.webcrawler.application.domain.crawler.controller;

import be.sandervl.webcrawler.application.domain.crawler.HttpCollectorBuilder;
import com.norconex.collector.http.HttpCollector;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final HttpCollectorBuilder httpCollectorBuilder;

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{siteId}")
    public String startCrawl(@PathVariable("siteId") Long siteId, @RequestBody(required = false) CrawlParams params) {
        var config = httpCollectorBuilder.getHttpCollectorConfig(siteId, params).orElseThrow();
        HttpCollector httpCollector = new HttpCollector(config);
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CrawlParams {
        @NotNull
        @NotBlank
        private String startingUrl;
        @Builder.Default
        private int amount = 100;
        @Builder.Default
        private int depth = 1;
    }
}
