package edu.java.scrapper.client.stackoverflow;

import edu.java.resilience.error.HttpClientErrorHandler;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class StackOverflowClientBuilder {
    private StackOverflowClientBuilder() {
    }

    public static StackOverflowClient build(
        RestClient.Builder builder,
        String baseUrl,
        HttpClientErrorHandler errorHandler
    ) {
        RestClient webClient = builder
            .baseUrl(baseUrl)
            .requestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build()))
            .defaultStatusHandler(errorHandler)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(webClient))
            .build();
        return proxyFactory.createClient(StackOverflowClient.class);
    }
}
