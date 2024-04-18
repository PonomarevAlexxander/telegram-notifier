package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.ScrapperClientBuilder;
import edu.java.resilience.error.HttpClientErrorHandler;
import edu.java.resilience.retry.LinearBackoffPolicy;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final RestClient.Builder builder;
    private final ApplicationConfig config;

    @Bean
    public ScrapperClient scrapperClient(ApplicationConfig config, HttpClientErrorHandler handler) {
        return ScrapperClientBuilder.build(builder, config.scrapperClient().baseUrl(), handler);
    }

    @Bean
    @Scope("singleton")
    public TelegramBot telegramBot(ApplicationConfig config) {
        return new TelegramBot(config.telegramToken());
    }

    @Bean
    public Set<HttpStatusCode> codes() {
        return config.scrapperClient().retry().codes();
    }

    @Bean
    public RetryOperationsInterceptor scrapperInterceptor() {
        return interceptor(config.scrapperClient().retry());
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
