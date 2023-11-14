package com.example.demo.config;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebConfig {

    private final RestClient.Builder restClientBuilder;

    public WebConfig(final RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Bean
    public RestClient firstRestClient(
            @Value("${first.baseUrl}") final String baseUrl,
            @Value("${first.username}") final String username,
            @Value("${first.password}") final String password
    ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, encodeBasic(username, password))
                //.requestFactory(createRequestFactory())
                .build();
    }

    private String encodeBasic(final String username, final String password) {
        return "Basic " + Base64
                .getEncoder()
                .encodeToString((username + ":" + password)
                .getBytes());
    }

    @Bean
    public RestTemplate secondRestTemplate(
            @Value("${second.baseUrl}") final String baseUrl,
            @Value("${second.username}") final String username,
            @Value("${second.password}") final String password
    ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return createRestTemplate(baseUrl, username, password);
    }

    private RestTemplate createRestTemplate(
            final String baseUrl,
            final String username,
            final String password
    ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final HttpComponentsClientHttpRequestFactory requestFactory = createRequestFactory();
        return new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
            .basicAuthentication(username, password)
            .requestFactory(() -> requestFactory)
            .build();
    }

    private HttpComponentsClientHttpRequestFactory createRequestFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        final SSLContext sslContext = SSLContexts.custom()
            .loadTrustMaterial(acceptingTrustStrategy)
            .build();

        final SSLConnectionSocketFactory csf = SSLConnectionSocketFactoryBuilder.create()
            .setHostnameVerifier(new NoopHostnameVerifier())
            .setSslContext(sslContext)
            .build();

        final PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(csf)
            .setDefaultSocketConfig(SocketConfig.custom().build())
            .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
            .setConnPoolPolicy(PoolReusePolicy.LIFO)
            .setDefaultConnectionConfig(ConnectionConfig.custom().build())
            .build();

        final RequestConfig requestConfig = RequestConfig.custom()
            .setCookieSpec(StandardCookieSpec.STRICT)
            .build();

        final CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

}
