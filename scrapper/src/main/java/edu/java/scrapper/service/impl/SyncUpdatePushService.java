package edu.java.scrapper.service.impl;

import edu.java.resilience.dto.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.service.UpdatePushService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class SyncUpdatePushService implements UpdatePushService {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdateRequest update) {
        botClient.sendUpdatesOnLink(update);
    }
}
