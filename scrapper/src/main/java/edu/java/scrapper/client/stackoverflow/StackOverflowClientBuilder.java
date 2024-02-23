package edu.java.scrapper.client.stackoverflow;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class StackOverflowClientBuilder {
    private StackOverflowClientBuilder() {
    }

    public static StackOverflowClient build(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        return proxyFactory.createClient(edu.java.scrapper.client.stackoverflow.StackOverflowClient.class);
    }
}
