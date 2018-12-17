package be.sandervl.webcrawler.application.config;

import com.norconex.collector.core.crawler.ICrawlerConfig;
import com.norconex.collector.http.HttpCollector;
import com.norconex.collector.http.HttpCollectorConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CollectorConfiguration {

    private final ICrawlerConfig[] crawlerConfigurations;

    @Qualifier("xmlCollectorConfiguration")
    private final HttpCollectorConfig xmlCollectorConfig;

    @Bean
    public HttpCollector getHttpCollector() {
        HttpCollectorConfig config = xmlCollectorConfig;
        if (xmlCollectorConfig == null) {
            config = new HttpCollectorConfig();
        }
        config.setCrawlerConfigs(crawlerConfigurations);
        config.setLogsDir("../data/crawler/logs");
        config.setProgressDir("../data/crawler/progress");
        return new HttpCollector(config);
    }
}
