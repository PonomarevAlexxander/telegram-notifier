package edu.java.bot.client;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ScrapperClientBuilder {
    private ScrapperClientBuilder() {
    }

    public static ScrapperClient build(RestClient.Builder builder, String baseUrl) {
        RestClient restClient = builder
            .baseUrl(baseUrl)
            .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build();
        return proxyFactory.createClient(ScrapperClient.class);
    }
}
