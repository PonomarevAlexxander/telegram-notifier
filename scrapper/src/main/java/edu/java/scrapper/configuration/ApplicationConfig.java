package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    public record StackOverflowClient(@NotBlank String baseUrl) {
    }

    public record GithubClient(@NotBlank String baseUrl) {
    }

    public record BotClient(@NotBlank String baseUrl) {
    }

    public enum AccessType {
        JDBC,
        JOOQ,
        JPA
    }
}
