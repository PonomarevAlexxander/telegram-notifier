package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableScheduling
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull
    StackOverflowClient stackOverflowClient,
    @NotNull
    GithubClient githubClient,
    @NotNull
    BotClient botClient,
    @NotNull
    AccessType databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record StackOverflowClient(@NotBlank String baseUrl, Retry retry) {
    }

    public record GithubClient(@NotBlank String baseUrl, Retry retry) {
    }

    public record BotClient(@NotBlank String baseUrl, Retry retry) {
    }

    public record Retry(Set<HttpStatusCode> codes, BackoffStrategy strategy, Duration delay, int maxAttempts) {
    }

    public enum BackoffStrategy {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }

    public enum AccessType {
        JDBC,
        JOOQ,
        JPA
    }
}
