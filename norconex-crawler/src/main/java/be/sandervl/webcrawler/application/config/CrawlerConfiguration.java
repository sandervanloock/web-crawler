package be.sandervl.webcrawler.application.config;

import com.norconex.collector.core.crawler.ICrawlerConfig;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.committer.core.ICommitter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class CrawlerConfiguration {

    private final HttpClient httpClient;
    private final ICommitter committer;

    @Qualifier("xmlCollectorConfiguration")
    private final HttpCollectorConfig xmlCollectorConfig;

    @Bean
    public ICrawlerConfig[] crawlerConfigs() {
        if (xmlCollectorConfig.getCrawlerConfigs().length == 0) {
            xmlCollectorConfig.setCrawlerConfigs(new HttpCrawlerConfig());
        }
        Arrays.stream(xmlCollectorConfig.getCrawlerConfigs()).forEach(this::setDefaultCrawlerConfigs);
        return xmlCollectorConfig.getCrawlerConfigs();
    }

    private void setDefaultCrawlerConfigs(ICrawlerConfig config) {
        if (config instanceof HttpCrawlerConfig) {
            HttpCrawlerConfig crawlerConfig = (HttpCrawlerConfig) config;
            crawlerConfig.setId("norconex-crawler");
            crawlerConfig.setHttpClientFactory(s -> httpClient);
            crawlerConfig.setCommitter(committer);

            URLCrawlScopeStrategy scopeStrategy = new URLCrawlScopeStrategy();
            scopeStrategy.setStayOnDomain(true);
            scopeStrategy.setStayOnPort(true);
            scopeStrategy.setStayOnProtocol(true);
            crawlerConfig.setUrlCrawlScopeStrategy(scopeStrategy);
            crawlerConfig.setWorkDir(new File("../data/crawler/work"));
        }
    }
}
