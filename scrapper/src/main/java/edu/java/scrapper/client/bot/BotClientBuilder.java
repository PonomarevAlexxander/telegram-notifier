package edu.java.scrapper.client.bot;

import edu.java.resilience.error.HttpClientErrorHandler;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class BotClientBuilder {
    private BotClientBuilder() {
    }

    public static BotClient build(RestClient.Builder builder, String baseUrl, HttpClientErrorHandler errorHandler) {
        RestClient webClient = builder
            .baseUrl(baseUrl)
            .requestFactory(new SimpleClientHttpRequestFactory())
            .defaultStatusHandler(errorHandler)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(webClient))
            .build();
        return proxyFactory.createClient(BotClient.class);
    }
}
