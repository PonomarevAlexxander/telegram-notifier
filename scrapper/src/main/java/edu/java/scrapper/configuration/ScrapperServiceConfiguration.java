package edu.java.scrapper.configuration;

import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.repository.TrackRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.UpdateService;
import edu.java.scrapper.service.impl.ScrapperChatService;
import edu.java.scrapper.service.impl.ScrapperLinkService;
import org.springframework.context.annotation.Bean;

public class ScrapperServiceConfiguration {

    @Bean
    ChatService chatService(ChatRepository chatRepository) {
        return new ScrapperChatService(chatRepository);
    }

    @Bean
    LinkService linkService(
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        TrackRepository trackRepository,
        UpdateService updateService
    ) {
        return new ScrapperLinkService(linkRepository, trackRepository, chatRepository, updateService);
    }
}
