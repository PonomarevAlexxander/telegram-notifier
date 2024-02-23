package edu.java.scrapper.configuration;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.GithubClientBuilder;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactory {
    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig config) {
        return StackOverflowClientBuilder.build(config.stackOverflowClient().baseUrl());
    }

    @Bean
    public GithubClient githubClient(ApplicationConfig config) {
        return GithubClientBuilder.build(config.githubClient().baseUrl());
    }
}
