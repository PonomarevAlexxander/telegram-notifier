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
import lombok.RequiredArgsConstructor;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ScrapperConfiguration {
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
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

}
