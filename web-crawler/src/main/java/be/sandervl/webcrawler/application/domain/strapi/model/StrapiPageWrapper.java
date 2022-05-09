package be.sandervl.webcrawler.application.domain.strapi.model;

import lombok.Data;

import java.util.List;

@Data
public class StrapiPageWrapper<T extends BaseStrapiObject> {
    private List<StrapiObjectWrapper<T>> data;
    private StrapiMeta meta;
}
