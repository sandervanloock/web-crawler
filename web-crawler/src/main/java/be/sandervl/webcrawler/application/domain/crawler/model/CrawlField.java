package be.sandervl.webcrawler.application.domain.crawler.model;

import be.sandervl.webcrawler.application.domain.strapi.model.BaseStrapiObject;
import lombok.Data;

@Data
public class CrawlField extends BaseStrapiObject {
    private String name;
    private String selector;
    private String extract;
}
