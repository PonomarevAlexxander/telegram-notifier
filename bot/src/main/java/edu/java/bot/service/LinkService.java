package edu.java.bot.service;

import edu.java.bot.domain.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    void trackLink(Link link);

    void deleteLink(Link link);

    List<URI> getTracked(Long userId);

    boolean isSupported(Link link);
}
