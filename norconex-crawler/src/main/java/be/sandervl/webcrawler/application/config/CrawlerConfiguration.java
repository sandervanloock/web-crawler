package be.sandervl.webcrawler.application.config;

import com.norconex.collector.core.crawler.ICrawlerConfig;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class CrawlerConfiguration {

    private final BeanFactory beanFactory;

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
            crawlerConfig.setId(config.getId());
            crawlerConfig.setHttpClientFactory(s -> beanFactory.getBean(HttpClient.class));
            crawlerConfig.setMaxDepth(CrawlerConstants.MAX_DEPTH);
            crawlerConfig.setMaxDocuments(CrawlerConstants.MAX_DOCUMENTS);

            ElasticsearchCommitter committer = beanFactory.getBean(ElasticsearchCommitter.class);
            committer.setIndexName(config.getId());
            crawlerConfig.setCommitter(committer);

            URLCrawlScopeStrategy scopeStrategy = new URLCrawlScopeStrategy();
            scopeStrategy.setStayOnDomain(true);
            scopeStrategy.setStayOnPort(true);
            scopeStrategy.setStayOnProtocol(true);
            crawlerConfig.setUrlCrawlScopeStrategy(scopeStrategy);
            crawlerConfig.setWorkDir(new File(CrawlerConstants.WORK_DIRECTORY));
        }
    }
}
