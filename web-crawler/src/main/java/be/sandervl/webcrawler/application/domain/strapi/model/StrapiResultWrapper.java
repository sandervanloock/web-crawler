package be.sandervl.webcrawler.application.domain.strapi.model;

import lombok.Data;

@Data
public class StrapiResultWrapper<T extends BaseStrapiObject> {
    private StrapiObjectWrapper<T> data;
    private StrapiMeta meta;
}
