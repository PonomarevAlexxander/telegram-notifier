package edu.java.bot.client;

import edu.java.resilience.error.HttpClientErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ScrapperClientBuilder {
    private ScrapperClientBuilder() {
    }

    public static ScrapperClient build(
        RestClient.Builder builder,
        String baseUrl,
        HttpClientErrorHandler errorHandler
    ) {
        RestClient restClient = builder
            .defaultStatusHandler(errorHandler)
            .baseUrl(baseUrl)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build();
        return proxyFactory.createClient(ScrapperClient.class);
    }
}
