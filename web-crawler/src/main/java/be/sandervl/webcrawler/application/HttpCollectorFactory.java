package be.sandervl.webcrawler.application;

import be.sandervl.webcrawler.application.config.CrawlerConstants;
import com.norconex.collector.core.CollectorConfigLoader;
import com.norconex.collector.core.crawler.ICrawlerConfig;
import com.norconex.collector.http.HttpCollector;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class HttpCollectorFactory {

    private final BeanFactory beanFactory;

    public HttpCollector createFromConfigString(String input) throws IOException {
        HttpCollectorConfig config = createFromString(input);
        config.setLogsDir(CrawlerConstants.LOGS_DIRECTORY + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyddMMhhmmss")));
        config.setProgressDir(CrawlerConstants.PROGRESS_DIRECTORY);
        return new HttpCollector(config);
    }

    private HttpCollectorConfig createFromString(String input) throws IOException {
        File configXml = File.createTempFile(RandomStringUtils.randomAlphanumeric(10), "xml");
        IOUtils.copy(new ByteArrayInputStream(input.getBytes()), new FileOutputStream(configXml));
        HttpCollectorConfig httpCollectorConfig = (HttpCollectorConfig) new CollectorConfigLoader(HttpCollectorConfig.class)
                .loadCollectorConfig(configXml);
        Arrays.stream(httpCollectorConfig.getCrawlerConfigs()).forEach(this::setDefaultCrawlerConfigs);
        return httpCollectorConfig;
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
            crawlerConfig.setWorkDir(new File(CrawlerConstants.WORK_DIRECTORY + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyddMMhhmmss"))));
        }
    }
}
