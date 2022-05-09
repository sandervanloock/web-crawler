package be.sandervl.webcrawler.application.domain.site.model;

import be.sandervl.webcrawler.application.domain.crawler.model.CrawlField;
import be.sandervl.webcrawler.application.domain.strapi.model.BaseStrapiObject;
import be.sandervl.webcrawler.application.domain.strapi.model.StrapiPageWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Site extends BaseStrapiObject {
    private String name;
    private String linkExtractorSelector;
    @JsonProperty("crawlFields")
    private StrapiPageWrapper<CrawlField> crawlFields;
}
