package be.sandervl.webcrawler.application.domain.site;

import be.sandervl.webcrawler.application.domain.site.model.Site;
import be.sandervl.webcrawler.application.domain.strapi.model.StrapiResultWrapper;
import lombok.val;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class SiteService {

    private final RestTemplate restTemplate;

    public SiteService() {
        this.restTemplate = new RestTemplate();
    }

    public Optional<StrapiResultWrapper<Site>> getSite(Long siteId) {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Authorization", "bearer f1d07c7fdd2075b2ddb61bcd346860219107bfe9e3352882fe66c3425a26ca7fe643fc4cdeaf250cd8920388d7ea1fd48cd5dafae5f7ca683f5291fc503de66a90d4ee1c4f77f4f01f0ce2e68dda98087914e87a5e7a1fd04c3d858bd210b94483eac6976f4d687cf95fd1d0f3a52bf9700a477242c67b248c550f0e03de4d93");

            val requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI("http://localhost:10337/api/sites/" + siteId + "?populate=crawlFields"));
            val result = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<StrapiResultWrapper<Site>>() {
            });
            return Optional.ofNullable(result.getBody());
        } catch (URISyntaxException | RestClientException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}

