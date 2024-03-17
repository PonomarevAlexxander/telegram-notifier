package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.ScrapperClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final WebClient.Builder builder;

    @Bean
    public ScrapperClient scrapperClient(ApplicationConfig config) {
        return ScrapperClientBuilder.build(builder, config.scrapperClient().baseUrl());
    }
}
