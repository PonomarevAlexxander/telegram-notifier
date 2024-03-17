package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.dto.LinkDTO;
import edu.java.scrapper.exception.LinkIsNotSupportedException;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.repository.TrackRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.UpdateService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapperLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TrackRepository trackRepository;
    private final ChatRepository chatRepository;
    private final UpdateService updateService;

    @Override
    @Transactional
    public List<LinkDTO> getAll(Long chatId) {
        return linkRepository.getAllLByChatId(chatId).stream()
            .map(link -> new LinkDTO(link.getId(), link.getUri().toString()))
            .toList();
    }

    @Override
    @Transactional
    public Long trackNew(Long chatId, String url) {
        if (!updateService.supports(url)) {
            throw new LinkIsNotSupportedException(
                String.format("Link %s is not supported yet", url)
            );
        }
        Long linkId;
        try {
            linkId = linkRepository.add(new Link(null, URI.create(url), OffsetDateTime.now()));
        } catch (ResourceAlreadyExistException e) {
            linkId = linkRepository.getByUri(url).getId();
        }

        trackRepository.add(new TrackRecord(chatId, linkId));
        return linkId;
    }

    @Override
    @Transactional
    public Long untrack(Long chatId, String url) {
        Long linkId = linkRepository.getByUri(url).getId();
        trackRepository.delete(new TrackRecord(chatId, linkId));

        if (chatRepository.getAllByUrl(url).isEmpty()) {
            linkRepository.delete(linkId);
        }
        return linkId;
    }
}
