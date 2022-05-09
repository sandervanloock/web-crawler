package be.sandervl.webcrawler.application.domain.strapi.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseStrapiObject {
    //        private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
