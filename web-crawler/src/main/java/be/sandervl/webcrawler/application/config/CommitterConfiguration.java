package be.sandervl.webcrawler.application.config;

import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CommitterConfiguration {

    @Bean
    @Scope("prototype")
    public ElasticsearchCommitter getElasticSearchCommitter() {
        ElasticsearchCommitter committer = new ElasticsearchCommitter();
        committer.setTypeName("web");
        committer.setTargetContentField(CrawlerConstants.TARGET_CONTENT_FIELD);
        committer.setNodes(CrawlerConstants.ELASTICSEARCH_URL);
        committer.setCommitBatchSize(CrawlerConstants.COMMIT_BATCH_SIZE);
        committer.setQueueDir(CrawlerConstants.QUEUE_DIRECTORY);
        committer.setFixBadIds(true);
        return committer;
    }
}
