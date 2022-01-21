package be.sandervl.webcrawler.application;

import com.norconex.collector.core.crawler.CrawlerConfig;
import com.norconex.collector.http.HttpCollector;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.collector.http.fetch.IHttpFetcher;
import com.norconex.committer.core3.batch.queue.impl.FSQueue;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import com.norconex.commons.lang.xml.XML;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class HttpCollectorFactory {

    private final BeanFactory beanFactory;

    public HttpCollector createFromConfigString(String input) throws IOException {
        HttpCollectorConfig config = createFromString(input);
//        config.setLogsDir(CrawlerConstants.LOGS_DIRECTORY + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyddMMhhmmss")));
//        config.setProgressDir(CrawlerConstants.PROGRESS_DIRECTORY);
        return new HttpCollector(config);
    }

    public static HttpCollectorConfig stringToConfig(String input) throws IOException {
        File configXml = File.createTempFile(RandomStringUtils.randomAlphanumeric(10), "xml");
        IOUtils.copy(new ByteArrayInputStream(input.getBytes()), new FileOutputStream(configXml));
        HttpCollectorConfig httpCollectorConfig = new HttpCollectorConfig();
        httpCollectorConfig.loadFromXML(XML.of(configXml).create());
        return httpCollectorConfig;
    }

    private HttpCollectorConfig createFromString(String input) throws IOException {
        HttpCollectorConfig httpCollectorConfig = stringToConfig(input);
        httpCollectorConfig.setWorkDir(Paths.get("./data"));
        httpCollectorConfig.getCrawlerConfigs().forEach(this::setDefaultCrawlerConfigs);
        return httpCollectorConfig;
    }

    private void setDefaultCrawlerConfigs(CrawlerConfig config) {
        if (config instanceof HttpCrawlerConfig) {
            HttpCrawlerConfig crawlerConfig = (HttpCrawlerConfig) config;
            String configId = config.getId();
            crawlerConfig.setId(configId + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            crawlerConfig.setHttpFetchers(beanFactory.getBean(IHttpFetcher.class));

            ElasticsearchCommitter committer = beanFactory.getBean(ElasticsearchCommitter.class);
            committer.setIndexName(getIndexNameForConfig(configId));
            committer.setDotReplacement("_");
            committer.setFixBadIds(true);
            committer.setTargetContentField("body");
            FSQueue committerQueue = new FSQueue();
            committerQueue.setBatchSize(3);
            committer.setCommitterQueue(committerQueue);
            crawlerConfig.setCommitters(committer);

            URLCrawlScopeStrategy scopeStrategy = new URLCrawlScopeStrategy();
            scopeStrategy.setStayOnDomain(true);
            scopeStrategy.setStayOnPort(true);
            scopeStrategy.setStayOnProtocol(true);
            crawlerConfig.setUrlCrawlScopeStrategy(scopeStrategy);
//            crawlerConfig.setWorkDir(new File(CrawlerConstants.WORK_DIRECTORY + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyddMMhhmmss"))));
        }
    }

    private String getIndexNameForConfig(String configId) {
        return String.format("crawl-%s-data-stream", configId);
    }
}
