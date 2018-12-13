package be.sandervl.webcrawler.application.config;

import com.norconex.collector.core.CollectorConfigLoader;
import com.norconex.collector.http.HttpCollectorConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class XmlCollectorConfiguration {

    @Bean
    @Qualifier("xmlCollectorConfiguration")
    public HttpCollectorConfig xmlCollectorConfig() throws IOException {
        return (HttpCollectorConfig) new CollectorConfigLoader(HttpCollectorConfig.class)
                .loadCollectorConfig(new ClassPathResource("crawler-config.xml").getFile(), null);
    }

}
