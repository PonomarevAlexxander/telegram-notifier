package edu.java.scrapper.service;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import edu.java.scrapper.dto.LinkUpdateDTO;
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
    private final UpdateFetchService updateFetchService;
    private final LinkService linkService;
    private final ChatService chatService;
    private final ApplicationConfig config;
    private final UpdatePushService updatePushService;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        OffsetDateTime trigger = OffsetDateTime.now().minus(config.scheduler().forceCheckDelay());
        List<Link> toUpdate = linkService.getAllBefore(trigger);
        List<LinkUpdate> updates = updateFetchService.fetchUpdates(toUpdate);
        for (var update : updates) {
            Link link = update.link();
            List<Long> chats = chatService.getAllByUrl(link.getUri().toString()).stream()
                .map(Chat::getId)
                .toList();
            updatePushService.sendUpdate(new LinkUpdateDTO(link.getId(), link.getUri(), update.updateInfo(), chats));
        }
        linkService.updateLastTrackedTime(toUpdate.stream()
            .map(Link::getId)
            .toList(), OffsetDateTime.now());
    }
}
