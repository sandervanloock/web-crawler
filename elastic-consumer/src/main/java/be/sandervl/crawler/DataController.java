package be.sandervl.crawler;

import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class DataController {
    private ElasticsearchOperations elasticsearchOperations;
    private SiteService siteService;

    public DataController(ElasticsearchOperations elasticsearchOperations, SiteService siteService) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.siteService = siteService;
    }

    @GetMapping("/data")
    public Flux<DataResult> search(@RequestParam("siteId") Long siteId, Pageable page) {
        return siteService.getSite(siteId).map(site -> {
            String siteName = site.getData().getAttributes().getName();
            CriteriaQuery query = new CriteriaQuery(Criteria.and());
            val fields = site.getData()
                    .getAttributes()
                    .getCrawlFields()
                    .getData()
                    .stream()
                    .map(cf -> String.format("%s", cf.getAttributes().getName().replace(".", "_")))
                    .collect(Collectors.toList());
            fields.add("@timestamp");
            query.addFields(fields.toArray(new String[0]));
            query.setPageable(page);
            query.addSort(Sort.by(Sort.Direction.DESC, "@timestamp"));
            SearchHits<DataResult> result = elasticsearchOperations.search(query, DataResult.class, IndexCoordinates.of("crawl-" + siteName + "-data-stream"));
            Stream<DataResult> contentStream = result.stream().map(SearchHit::getContent);
            return Flux.fromStream(contentStream);
        }).orElse(Flux.empty());

    }

    static class DataResult extends HashMap<String, String> {

    }
}
