package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.BotClientBuilder;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.GithubClientBuilder;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClientBuilder;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ScrapperConfiguration {
    private final WebClient.Builder builder;
    private final ApplicationConfig config;

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig config) {
        return StackOverflowClientBuilder.build(builder, config.stackOverflowClient().baseUrl());
    }

    @Bean
    public GithubClient githubClient(ApplicationConfig config) {
        return GithubClientBuilder.build(builder, config.githubClient().baseUrl());
    }

    @Bean
    public BotClient botClient(ApplicationConfig config) {
        return BotClientBuilder.build(builder, config.botClient().baseUrl());
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }


    @Bean
    public RowMapper<Chat> chatRowMapper() {
        return (rs, rowNum) -> new Chat(rs.getLong(1));
    }

    @Bean
    @SuppressWarnings("MagicNumber")
    public RowMapper<Link> linkRowMapper() {
        return (rs, rowNum) -> new Link(
            rs.getLong(1),
            URI.create(rs.getString(2)),
            rs.getObject(3, OffsetDateTime.class)
        );
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

    private RetryOperationsInterceptor interceptor(ApplicationConfig.Retry retry) {
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
