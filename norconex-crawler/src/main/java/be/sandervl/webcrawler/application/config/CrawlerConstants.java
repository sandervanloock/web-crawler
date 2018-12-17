package be.sandervl.webcrawler.application.config;

public interface CrawlerConstants {
    int MAX_DEPTH = 2;
    int MAX_DOCUMENTS = 200;
    String ELASTICSEARCH_URL = "http://localhost:9200";
    int COMMIT_BATCH_SIZE = 1;
    String TARGET_CONTENT_FIELD = "body";
}
