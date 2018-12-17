package be.sandervl.webcrawler.application.config;

import com.norconex.committer.core.ICommitter;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope("prototype")
public class CommitterConfiguration {
    @Bean
    public ElasticsearchCommitter getElasticSearchCommitter() {
        ElasticsearchCommitter committer = new ElasticsearchCommitter();
        committer.setTypeName("web");
        committer.setTargetContentField("body");
        committer.setNodes("http://localhost:9200");
        committer.setCommitBatchSize(1);
        committer.setQueueDir("../data/committer");
        committer.setFixBadIds(true);
        return committer;
    }
}
