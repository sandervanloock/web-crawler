package be.sandervl.webcrawler.application.config;

public interface CrawlerConstants {
    String ELASTICSEARCH_URL = "http://localhost:9200";

    int MAX_DEPTH = 10;
    int MAX_DOCUMENTS = 1000;
    int COMMIT_BATCH_SIZE = 3;

    String TARGET_CONTENT_FIELD = "body";

    String QUEUE_DIRECTORY = "../data/committer";
    String WORK_DIRECTORY = "../data/crawler/work";
    String LOGS_DIRECTORY = "../data/crawler/logs";
    String PROGRESS_DIRECTORY = "../data/crawler/progress";
}
