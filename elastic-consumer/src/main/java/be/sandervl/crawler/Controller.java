package be.sandervl.crawler;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")
public class Controller {
    private ElasticsearchOperations elasticsearchOperations;

    public Controller(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping("/vrtnws")
    public Flux<Document> search() {
        Query query = Query.findAll();
        SearchHits<Document> person = elasticsearchOperations.search(query, Document.class);
        return Flux.fromStream(person.get().map(SearchHit::getContent));
    }

    @org.springframework.data.elasticsearch.annotations.Document(indexName = "crawl-vrtnws-data-stream")
    public class Document {

        @Id
        String id;
        String title;
        @Field("@timestamp")
        String crawlDate;
        @Field("body")
        String body;

        public Document() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCrawlDate() {
            return crawlDate;
        }

        public void setCrawlDate(String crawlDate) {
            this.crawlDate = crawlDate;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
