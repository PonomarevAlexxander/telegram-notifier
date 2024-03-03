package edu.java.bot.service.notifier;

import edu.java.bot.domain.Link;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotifierLinkService implements LinkService {
    @Override
    public void trackLink(Link link) {

    }

    @Override
    public void deleteLink(Link link) {

    }

    @Override
    public List<URI> getTracked(Long userId) {
        return null;
    }

    @Override
    public boolean isSupported(Link link) {
        return false;
    }
}
