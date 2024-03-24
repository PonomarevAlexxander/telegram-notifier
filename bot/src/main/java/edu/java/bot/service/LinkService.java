package edu.java.bot.service;

import java.net.URI;
import java.util.List;

public interface LinkService {
    void trackLink(Long chatId, URI link);

    void untrackLink(Long chatId, URI link);

    List<URI> getTracked(Long userId);
}
