package edu.java.scrapper.service.impl;

import edu.java.scrapper.client.UpdateHandler;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import edu.java.scrapper.service.UpdateFetchService;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperUpdateFetchService implements UpdateFetchService {
    private final List<UpdateHandler> handlers;

    @Override
    public boolean supports(String link) {
        for (var handler : handlers) {
            if (handler.supports(link)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<LinkUpdate> fetchUpdates(List<Link> toUpdate) {
        List<LinkUpdate> updates = new LinkedList<>();
        for (var link : toUpdate) {
            for (var handler : handlers) {
                if (handler.supports(link.getUri().toString())) {
                    LinkUpdate update = handler.getUpdate(link);
                    if (update.updated()) {
                        updates.add(update);
                    }
                }
            }
        }

        return updates;
    }
}
