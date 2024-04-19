package edu.java.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    String updateTopic,
    Boolean useQueue
) {
    public record ScrapperClient(@NotBlank String baseUrl, Retry retry) {
    }

    public record Retry(Set<HttpStatusCode> codes, BackoffStrategy strategy, Duration delay, int maxAttempts) {
    }

    public enum BackoffStrategy {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }
}
