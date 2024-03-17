package edu.java.scrapper.service;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.LinkUpdateRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "app.scheduler.enable")
public class LinkScheduledUpdater {
    private final UpdateService updateService;
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final ApplicationConfig config;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        OffsetDateTime trigger = OffsetDateTime.now().minus(config.scheduler().forceCheckDelay());
        List<Link> toUpdate = linkRepository.getAllBefore(trigger);
        List<LinkUpdate> updates = updateService.fetchUpdates(toUpdate);
        for (var update : updates) {
            Link link = update.link();
            List<Long> chats = chatRepository.getAllByUrl(link.getUri().toString()).stream()
                .map(Chat::getId)
                .toList();
            botClient.sendUpdatesOnLink(
                new LinkUpdateRequest(link.getId(), link.getUri(), update.updateInfo(), chats)
            );
        }
        linkRepository.updateLastTrackedTime(updates.stream()
            .map(update -> update.link().getId())
            .toList(), OffsetDateTime.now());
    }
}
