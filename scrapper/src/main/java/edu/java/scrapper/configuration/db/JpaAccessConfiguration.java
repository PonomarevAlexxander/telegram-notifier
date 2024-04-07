package edu.java.scrapper.configuration.db;

import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.UpdateFetchService;
import edu.java.scrapper.service.impl.JpaChatService;
import edu.java.scrapper.service.impl.JpaLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    ChatService chatService(JpaChatRepository chatRepository, JpaLinkRepository linkRepository) {
        return new JpaChatService(chatRepository, linkRepository);
    }

    @Bean
    LinkService linkService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        UpdateFetchService updateFetchService
    ) {
        return new JpaLinkService(linkRepository, chatRepository, updateFetchService);
    }
}
