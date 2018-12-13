package be.sandervl.webcrawler;

import com.norconex.collector.http.HttpCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class CrawlerRunner {

    private final HttpCollector httpCollector;

    @PostConstruct
    public void construct() {
        httpCollector.start(true);
    }
}
