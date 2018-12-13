package be.sandervl.webcrawler.application.config;

import com.norconex.committer.core.ICommitter;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommitterConfiguration {
    @Bean
    public ICommitter getCommitter() {
        ElasticsearchCommitter committer = new ElasticsearchCommitter();
        committer.setTypeName("web");
        committer.setIndexName("foreach");
        committer.setTargetContentField("body");
        committer.setNodes("http://localhost:9200");
        committer.setCommitBatchSize(1);
        committer.setQueueDir("../data/committer");
        return committer;
    }
}
