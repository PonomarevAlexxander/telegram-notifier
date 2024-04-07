package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.jpa.Chat;
import edu.java.scrapper.domain.jpa.Link;
import edu.java.scrapper.dto.LinkDTO;
import edu.java.scrapper.exception.LinkIsNotSupportedException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.UpdateFetchService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final UpdateFetchService updateFetchService;

    @Override
    @Transactional(readOnly = true)
    public List<LinkDTO> getAllByChatId(Long chatId) {
        return linkRepository.findAll().stream()
            .map(link -> new LinkDTO(link.getId(), link.getUri()))
            .toList();
    }

    @Override
    @Transactional
    public Long trackNew(Long chatId, String url) {
        if (!updateFetchService.supports(url)) {
            throw new LinkIsNotSupportedException(
                String.format("Link %s is not supported yet", url)
            );
        }
        URI uri = URI.create(url);

        Link link = linkRepository.getLinkByUri(uri.toString())
            .orElseGet(() -> {
                Link newLink = new Link();
                newLink.setUri(uri.toString());
                newLink.setLastTracked(OffsetDateTime.now());
                return linkRepository.save(newLink);
            });

        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ResourceNotExistException(String.format(
                "Chat with id %d is not registered",
                chatId
            )));

        chat.addLink(link);
        chatRepository.save(chat);
        return link.getId();
    }

    @Override
    @Transactional
    public Long untrack(Long chatId, String url) {
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ResourceNotExistException(String.format(
                "Chat with id %d isn't registered",
                chatId
            )));
        Link link = linkRepository.getLinkByUri(url)
            .orElseThrow(() -> new ResourceNotExistException(String.format("Link with url %s is not tracked", url)));
        chat.deleteLink(link);
        chatRepository.save(chat);
        if (link.getTrackingChats().isEmpty()) {
            linkRepository.delete(link);
        }
        return link.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<edu.java.scrapper.domain.Link> getAllBefore(OffsetDateTime time) {
        return linkRepository.findAllByLastTrackedIsBefore(time).stream()
            .map(link -> new edu.java.scrapper.domain.Link(
                link.getId(),
                URI.create(link.getUri()),
                link.getLastTracked()
            ))
            .toList();
    }

    @Override
    @Transactional
    public void updateLastTrackedTime(List<Long> ids, OffsetDateTime time) {
        linkRepository.updateLinksLastTracked(ids, time);
    }
}
