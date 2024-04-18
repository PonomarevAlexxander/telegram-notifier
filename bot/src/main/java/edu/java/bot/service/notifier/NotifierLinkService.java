package edu.java.bot.service.notifier;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.service.LinkService;
import edu.java.resilience.dto.LinkRequest;
import edu.java.resilience.dto.LinkResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifierLinkService implements LinkService {
    private final ScrapperClient client;

    @Override
    public void trackLink(Long chatId, URI link) {
        client.trackNew(chatId, new LinkRequest(link.toString()));
    }

    @Override
    public void untrackLink(Long chatId, URI link) {
        client.untrackLink(chatId, new LinkRequest(link.toString()));
    }

    @Override
    public List<URI> getTracked(Long chatId) {
        return client.getAllTrackedLinks(chatId).links().stream()
            .map(LinkResponse::url)
            .toList();
    }
}
