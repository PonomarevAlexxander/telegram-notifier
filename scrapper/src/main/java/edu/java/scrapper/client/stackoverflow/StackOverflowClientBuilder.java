package edu.java.scrapper.client.stackoverflow;

import edu.java.resilience.error.HttpClientErrorHandler;
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
            .defaultStatusHandler(errorHandler)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(webClient))
            .build();
        return proxyFactory.createClient(StackOverflowClient.class);
    }
}
