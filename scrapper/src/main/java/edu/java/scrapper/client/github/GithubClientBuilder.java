package edu.java.scrapper.client.github;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class GithubClientBuilder {
    private GithubClientBuilder() {
    }

    public static GithubClient build(WebClient.Builder builder, String baseUrl) {
        WebClient webClient = builder
            .baseUrl(baseUrl)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        return proxyFactory.createClient(GithubClient.class);
    }
}
