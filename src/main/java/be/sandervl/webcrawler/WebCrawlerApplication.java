package be.sandervl.webcrawler;

import com.norconex.collector.http.HttpCollector;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.committer.core.ICommitter;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class WebCrawlerApplication {

    @Bean
    static HttpClient httpClient() {
        final SSLConnectionSocketFactory sslsf;
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build(),
                    NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
    }

    @Bean
    static HttpCollector getHttpCollector() {
        HttpCollectorConfig config = new HttpCollectorConfig();
        config.setId("norconex");
        config.setCrawlerConfigs(getHttpCrawlerConfig());
        config.setLogsDir("target/crawler/logs");
        config.setProgressDir("target/crawler/progress");
        return new HttpCollector(config);
    }

    private static HttpCrawlerConfig getHttpCrawlerConfig() {
        HttpCrawlerConfig crawlerConfig = new HttpCrawlerConfig();
        crawlerConfig.setId("norconex-crawler");
        crawlerConfig.setStartURLs("https://www.foreach.be");
        crawlerConfig.setHttpClientFactory(s -> httpClient());
        crawlerConfig.setCommitter(getCommitter());
        crawlerConfig.setMaxDepth(2);
        //crawlerConfig.setWorkDir();

        URLCrawlScopeStrategy scopeStrategy = new URLCrawlScopeStrategy();
        scopeStrategy.setStayOnDomain(true);
        scopeStrategy.setStayOnPort(true);
        scopeStrategy.setStayOnProtocol(true);
        crawlerConfig.setUrlCrawlScopeStrategy(scopeStrategy);
        return crawlerConfig;
    }

    @Bean
    static ICommitter getCommitter() {
        ElasticsearchCommitter committer = new ElasticsearchCommitter();
        committer.setTypeName("web");
        committer.setIndexName("foreach");
        committer.setTargetContentField("body");
        committer.setNodes("http://localhost:9200");
        committer.setCommitBatchSize(1);
        return committer;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebCrawlerApplication.class, args);
    }

    @Component
    static class CrawlerRunner {
        @PostConstruct
        public void construct() {
            HttpCollector collector = getHttpCollector();
            collector.start(true);
        }
    }
}
