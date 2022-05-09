package be.sandervl.webcrawler.application.domain.strapi.model;

import lombok.Data;

@Data
public class StrapiPage {
    private int page;
    private int pageSize;
    private int pageCount;
    private int total;
}
