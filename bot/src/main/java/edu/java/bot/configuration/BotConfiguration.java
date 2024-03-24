package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.ScrapperClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final RestClient.Builder builder;

    @Bean
    public ScrapperClient scrapperClient(ApplicationConfig config) {
        return ScrapperClientBuilder.build(builder, config.scrapperClient().baseUrl());
    }

    @Bean
    @Scope("singleton")
    TelegramBot telegramBot(ApplicationConfig config) {
        return new TelegramBot(config.telegramToken());
    }
}
