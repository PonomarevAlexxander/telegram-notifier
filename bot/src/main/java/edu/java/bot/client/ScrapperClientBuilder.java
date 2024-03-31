package edu.java.bot.client;

import edu.java.bot.exception.ClientRetryException;
import java.util.Set;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ScrapperClientBuilder {
    private ScrapperClientBuilder() {
    }

    public static ScrapperClient build(
        RestClient.Builder builder,
        String baseUrl,
        ScrapperResponseErrorHandler errorHandler
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

    private ExchangeFilterFunction retryFilter(Set<HttpStatusCode> retryCodes) {
        return (request, next) ->
            next.exchange(request)
                .doOnNext(clientResponse -> {
                    if (retryCodes.contains(clientResponse.statusCode())) {
                        throw new ClientRetryException("Need to retry request");
                    }
                });
    }
}
