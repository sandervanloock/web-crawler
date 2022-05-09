package be.sandervl.webcrawler.application.domain.strapi.model;

import lombok.Data;

@Data
public class StrapiObjectWrapper<T extends BaseStrapiObject> {
    private Long id;
    private T attributes;
}
