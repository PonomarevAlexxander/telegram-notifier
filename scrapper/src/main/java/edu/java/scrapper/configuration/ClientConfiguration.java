package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.BotClientBuilder;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.GithubClientBuilder;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final WebClient.Builder builder;

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
}
