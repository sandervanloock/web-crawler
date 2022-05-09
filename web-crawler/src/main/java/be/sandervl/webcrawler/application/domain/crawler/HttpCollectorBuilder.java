package be.sandervl.webcrawler.application.domain.crawler;

import be.sandervl.webcrawler.application.domain.crawler.controller.CrawlerController;
import be.sandervl.webcrawler.application.domain.crawler.model.CrawlField;
import be.sandervl.webcrawler.application.domain.site.SiteService;
import be.sandervl.webcrawler.application.domain.site.model.Site;
import be.sandervl.webcrawler.application.domain.strapi.model.StrapiObjectWrapper;
import com.norconex.collector.core.crawler.CrawlerConfig;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.collector.http.fetch.IHttpFetcher;
import com.norconex.collector.http.link.impl.DOMLinkExtractor;
import com.norconex.collector.http.url.impl.GenericURLNormalizer;
import com.norconex.committer.core3.batch.queue.impl.FSQueue;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import com.norconex.commons.lang.map.PropertySetter;
import com.norconex.importer.Importer;
import com.norconex.importer.ImporterConfig;
import com.norconex.importer.handler.HandlerConsumer;
import com.norconex.importer.handler.IImporterHandler;
import com.norconex.importer.handler.tagger.impl.CurrentDateTagger;
import com.norconex.importer.handler.tagger.impl.DOMTagger;
import com.norconex.importer.handler.tagger.impl.URLExtractorTagger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HttpCollectorBuilder {

    public static final String TIMESTAMP_FIELD = "@timestamp";
    public static final String URL_FIELD = "url";

    private final BeanFactory beanFactory;
    private final SiteService siteService;

    public Optional<Importer> createImporter(HttpCollectorConfig config) {
        return config.getCrawlerConfigs().stream().findAny().map(CrawlerConfig::getImporterConfig).map(Importer::new);
    }

    public Optional<HttpCollectorConfig> getHttpCollectorConfig(Long siteId, CrawlerController.CrawlParams params) {
        var site = siteService.getSite(siteId).orElseThrow();
        Site siteAttributes = site.getData().getAttributes();

        HttpCollectorConfig config = new HttpCollectorConfig();
        String siteNameWithTimestamp = siteAttributes.getName() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        config.setId(siteNameWithTimestamp);

        HttpCrawlerConfig crawlerConfig = new HttpCrawlerConfig();
        crawlerConfig.setStartURLs(params.getStartingUrl());
        crawlerConfig.setUrlNormalizer(new GenericURLNormalizer());
        crawlerConfig.setNumThreads(2);
        crawlerConfig.setMaxDepth(params.getDepth());
        crawlerConfig.setMaxDocuments(params.getAmount());
        crawlerConfig.setOrphansStrategy(CrawlerConfig.OrphansStrategy.PROCESS);
        DOMLinkExtractor domLinkExtractor = new DOMLinkExtractor();
        domLinkExtractor.addExtractSelectors(siteAttributes.getLinkExtractorSelector());
        crawlerConfig.setLinkExtractors(domLinkExtractor);
        crawlerConfig.setImporterConfig(createImporterConfig(siteAttributes));
        crawlerConfig.setId(siteAttributes.getName());
        crawlerConfig.setHttpFetchers(beanFactory.getBean(IHttpFetcher.class));
        crawlerConfig.setIgnoreSitemap(true);
        crawlerConfig.setCommitters(createCommitter(siteAttributes.getName()));
        config.setCrawlerConfigs(crawlerConfig);
        URLCrawlScopeStrategy scopeStrategy = new URLCrawlScopeStrategy();
        scopeStrategy.setStayOnDomain(true);
        scopeStrategy.setStayOnPort(true);
        scopeStrategy.setStayOnProtocol(true);
        crawlerConfig.setUrlCrawlScopeStrategy(scopeStrategy);

        return Optional.of(config);
    }

    private ImporterConfig createImporterConfig(Site site) {
        List<IImporterHandler> handlers = createHandlersForSite(site);
        ImporterConfig importerConfig = new ImporterConfig();
        importerConfig.setPreParseConsumer(HandlerConsumer.fromHandlers(handlers));
//        DeleteTagger deleteTagger = new DeleteTagger();
//        deleteTagger.setFieldMatcher(new TextMatcher("(^(?!vrtnws|@timestamp).+$)", TextMatcher.Method.REGEX));
//        importerConfig.setPostParseConsumer(HandlerConsumer.fromHandlers(List.of(deleteTagger)));
        return importerConfig;
    }

    private ElasticsearchCommitter createCommitter(String id) {
        ElasticsearchCommitter committer = beanFactory.getBean(ElasticsearchCommitter.class);
        committer.setIndexName(getIndexNameForConfig(id));
        committer.setDotReplacement("_");
        committer.setFixBadIds(true);
        committer.setTargetContentField("body");
        committer.setSourceIdField("vrtnws_url");
        FSQueue committerQueue = new FSQueue();
        committerQueue.setBatchSize(3);
        committer.setCommitterQueue(committerQueue);
        return committer;
    }

    private String getIndexNameForConfig(String configId) {
        return String.format("crawl-%s-data-stream", configId);
    }

    private List<IImporterHandler> createHandlersForSite(Site siteAttributes) {
        List<IImporterHandler> result = siteAttributes.getCrawlFields().getData().stream()
                .map(StrapiObjectWrapper::getAttributes)
                .map(this::createDomTagger)
                .collect(Collectors.toList());
        CurrentDateTagger currentDateTagger = new CurrentDateTagger();
        currentDateTagger.setToField(TIMESTAMP_FIELD);
        result.add(currentDateTagger);
        URLExtractorTagger urlExtractorTagger = new URLExtractorTagger();
        urlExtractorTagger.setToField(URL_FIELD);
        result.add(urlExtractorTagger);
        return result;
    }

    private DOMTagger createDomTagger(CrawlField crawlField) {
        DOMTagger domTagger = new DOMTagger();
        domTagger.addDOMExtractDetails(new DOMTagger.DOMExtractDetails(
                crawlField.getSelector(),
                crawlField.getName(),
                PropertySetter.REPLACE,
                crawlField.getExtract()));
        return domTagger;
    }

}
