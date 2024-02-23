package edu.java.scrapper.service;

import edu.java.scrapper.client.UpdateHandler;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperUpdateService implements UpdateService {
    private final List<UpdateHandler> handlers;

    @Override
    public List<LinkUpdate> fetchUpdates(List<Link> toUpdate) {
        List<LinkUpdate> updates = new LinkedList<>();
        for (var link : toUpdate) {
            for (var handler : handlers) {
                if (handler.supports(link.resource().getPath())) {
                    updates.add(handler.getUpdate(link));
                }
            }
        }

        return updates.stream()
            .filter(LinkUpdate::updated)
            .toList();
    }
}
