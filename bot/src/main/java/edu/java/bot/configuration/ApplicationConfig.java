package edu.java.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull
    ScrapperClient scrapperClient,
    KafkaConsumer kafkaConsumer,
    String updateTopic,
    Boolean useQueue,
    KafkaProducer kafkaProducer
) {
    public record ScrapperClient(@NotBlank String baseUrl, Retry retry) {
    }

    public record Retry(Set<HttpStatusCode> codes, BackoffStrategy strategy, Duration delay, int maxAttempts) {
    }

    public record KafkaConsumer(
        @NotBlank
        String servers,
        @NotBlank
        String groupId,
        @NotBlank
        String autoOffsetReset,
        Boolean enableAutoCommit,
        Integer concurrency,
        Duration maxPollInterval,
        Duration backoffInterval,
        Integer backoffAttempts
    ) {
    }

    public record KafkaProducer(
        @NotBlank
        String servers,
        @NotBlank
        String clientId,
        @NotBlank
        String acks,
        @NotNull
        Duration lingerMs,
        @PositiveOrZero
        Integer batchSize
    ) {
    }

    public enum BackoffStrategy {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }
}
