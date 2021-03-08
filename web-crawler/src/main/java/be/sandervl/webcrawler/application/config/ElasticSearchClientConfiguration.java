package be.sandervl.webcrawler.application.config;

import com.norconex.commons.lang.encrypt.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

import static com.norconex.committer.elasticsearch.ElasticsearchCommitter.DEFAULT_CONNECTION_TIMEOUT;
import static com.norconex.committer.elasticsearch.ElasticsearchCommitter.DEFAULT_SOCKET_TIMEOUT;

@Configuration
@Slf4j
public class ElasticSearchClientConfiguration {

    @Bean
    public RestClient createRestClient() {
        List<String> elasticHosts = Arrays.asList(CrawlerConstants.ELASTICSEARCH_URL);
        HttpHost[] httpHosts = new HttpHost[elasticHosts.size()];
        for (int i = 0; i < elasticHosts.size(); i++) {
            httpHosts[i] = HttpHost.create(elasticHosts.get(i));
        }

        RestClientBuilder builder = RestClient.builder(httpHosts);
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                log.error("Failure occured on node: \"{}\". Check node logs.",
                        node.getName());
            }
        });
        builder.setRequestConfigCallback(rcb -> rcb
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT));

//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        credsProvider.setCredentials(
//                AuthScope.ANY, new UsernamePasswordCredentials(
//                        credentials.getUsername(), EncryptionUtil.decrypt(
//                        credentials.getPassword(),
//                        credentials.getPasswordKey())));
//        builder.setHttpClientConfigCallback(
//                b -> b.setDefaultCredentialsProvider(credsProvider));
        return builder.build();
    }
}
