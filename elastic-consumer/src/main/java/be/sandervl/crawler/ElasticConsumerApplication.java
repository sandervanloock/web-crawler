package be.sandervl.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@SpringBootApplication
public class ElasticConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticConsumerApplication.class, args);
	}

	@Bean
	public ReactiveElasticsearchClient client() {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo("localhost:9200", "localhost:9291")
				.withWebClientConfigurer(webClient -> {
					ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
							.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
							.build();
					return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
				})
				.build();

		return ReactiveRestClients.create(clientConfiguration);
	}

}
