package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
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
    GithubClient githubClient
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record StackOverflowClient(@Value("${app.stackoverflowclient.baseurl:https://api.stackexchange.com}") String baseUrl) {
    }

    public record GithubClient(String baseUrl) {
    }
}
