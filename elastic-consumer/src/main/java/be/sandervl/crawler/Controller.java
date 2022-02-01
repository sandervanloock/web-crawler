package be.sandervl.crawler;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class Controller {
    private ElasticsearchOperations elasticsearchOperations;

    public Controller(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping("/data")
    public Flux<Object> search(Pageable page) {

        String site = "vrtnws"; //TODO get site name from CMS

        CriteriaQuery query = new CriteriaQuery(Criteria.and());
        query.addFields("vrtnws_url", "vrtnws_title", "vrtnws_img", "vrtnws_authors", "vrtnws_body", "@timestamp"); //TODO get fields from CMS
        query.setPageable(page);
        query.addSort(Sort.by(Sort.Direction.DESC, "@timestamp"));
        SearchHits<Map> result = elasticsearchOperations.search(query, Map.class, IndexCoordinates.of("crawl-" + site + "-data-stream"));

        return Flux.fromStream(result.stream().map(SearchHit::getContent));
    }
}
