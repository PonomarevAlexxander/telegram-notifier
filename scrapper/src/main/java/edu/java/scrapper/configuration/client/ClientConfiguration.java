package edu.java.scrapper.configuration.client;

import edu.java.resilience.error.HttpClientErrorHandler;
import edu.java.resilience.retry.LinearBackoffPolicy;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.BotClientBuilder;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.GithubClientBuilder;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClientBuilder;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final RestClient.Builder builder;
    private final ApplicationConfig config;

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig config, HttpClientErrorHandler errorHandler) {
        return StackOverflowClientBuilder.build(builder, config.stackOverflowClient().baseUrl(), errorHandler);
    }

    @Bean
    public GithubClient githubClient(ApplicationConfig config, HttpClientErrorHandler errorHandler) {
        return GithubClientBuilder.build(builder, config.githubClient().baseUrl(), errorHandler);
    }

    @Bean
    public BotClient botClient(ApplicationConfig config, HttpClientErrorHandler errorHandler) {
        return BotClientBuilder.build(builder, config.botClient().baseUrl(), errorHandler);
    }

    @Bean
    public Set<HttpStatusCode> codes() {
        Set<HttpStatusCode> codes = config.stackOverflowClient().retry().codes();
        codes.addAll(config.githubClient().retry().codes());
        codes.addAll(config.stackOverflowClient().retry().codes());
        codes.addAll(config.botClient().retry().codes());
        return codes;
    }

    @Bean
    public RetryOperationsInterceptor botInterceptor() {
        return interceptor(config.botClient().retry());
    }

    @Bean
    public RetryOperationsInterceptor githubInterceptor() {
        return interceptor(config.githubClient().retry());
    }

    @Bean
    public RetryOperationsInterceptor stackOverflowInterceptor() {
        return interceptor(config.stackOverflowClient().retry());
    }

    public static RetryOperationsInterceptor interceptor(ApplicationConfig.Retry retry) {
        BackOffPolicy backOffPolicy = switch (retry.strategy()) {
            case EXPONENTIAL -> {
                ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
                policy.setInitialInterval(retry.delay().toMillis());
                yield policy;
            }
            case CONSTANT -> {
                FixedBackOffPolicy policy = new FixedBackOffPolicy();
                policy.setBackOffPeriod(retry.delay().toMillis());
                yield policy;
            }
            case LINEAR -> {
                LinearBackoffPolicy policy = new LinearBackoffPolicy();
                policy.setInitialInterval(retry.delay().toMillis());
                yield policy;
            }
            default -> throw new IllegalStateException("Unexpected value: " + retry.strategy());
        };

        return RetryInterceptorBuilder.stateless()
            .backOffPolicy(backOffPolicy)
            .maxAttempts(retry.maxAttempts())
            .build();
    }
}
