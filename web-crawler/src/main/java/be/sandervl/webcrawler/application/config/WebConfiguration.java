package be.sandervl.webcrawler.application.config;

import com.norconex.collector.http.fetch.IHttpFetcher;
import com.norconex.collector.http.fetch.impl.GenericHttpFetcher;
import com.norconex.collector.http.fetch.impl.GenericHttpFetcherConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class WebConfiguration {

    @Bean
    public IHttpFetcher httpFetcher(){
        GenericHttpFetcherConfig config = new GenericHttpFetcherConfig();
        config.setTrustAllSSLCertificates(true);
        GenericHttpFetcher genericHttpFetcher = new GenericHttpFetcher(config);
        return genericHttpFetcher;
    }
}
