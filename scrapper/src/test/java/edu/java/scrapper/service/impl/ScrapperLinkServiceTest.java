package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.dto.LinkDTO;
import edu.java.scrapper.exception.LinkIsNotSupportedException;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.repository.TrackRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.UpdateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScrapperLinkServiceTest {
    @Mock
    LinkRepository linkRepository;
    @Mock
    TrackRepository trackRepository;
    @Mock
    ChatRepository chatRepository;
    @Mock
    UpdateService updateService;
    @InjectMocks
    ScrapperLinkService linkService;

    @Test
    @DisplayName("Test getAll() with existing Chat")
    void getAll_existing() {
        List<Link> links = List.of(
            new Link(1L, URI.create("https://github.com"), OffsetDateTime.now()),
            new Link(1L, URI.create("https://another.com"), OffsetDateTime.now())
        );

        Mockito.when(linkRepository.getAllLByChatId(1L)).thenReturn(links);

        assertThat(linkService.getAll(1L))
            .asList()
            .containsExactly(new LinkDTO(1L, "https://github.com"), new LinkDTO(1L, "https://another.com"));
    }

    @Test
    @DisplayName("Test getAll() with not existing Chat")
    void getAll_not_existing() {
        Mockito.when(linkRepository.getAllLByChatId(Mockito.anyLong())).thenReturn(new LinkedList<>());

        assertThat(linkService.getAll(1L))
            .asList()
            .isEmpty();
    }

    @Test
    @DisplayName("Test trackNew() with not supported Link")
    void trackNew_not_supported() {
        Mockito.when(updateService.supports(Mockito.anyString())).thenReturn(false);

        assertThatThrownBy(() -> linkService.trackNew(1L, "https://some.com"))
            .isInstanceOf(LinkIsNotSupportedException.class);
    }

    @Test
    @DisplayName("Test trackNew() with existing Link")
    void trackNew_existing_link() {
        String url = "http://some.com";

        Mockito.when(updateService.supports(Mockito.eq(url))).thenReturn(true);
        Mockito.when(linkRepository.add(Mockito.any(Link.class))).thenThrow(ResourceAlreadyExistException.class);
        Link link = new Link(21L, URI.create(url), OffsetDateTime.now());
        Mockito.when(linkRepository.getByUri(Mockito.eq(url)))
            .thenReturn(link);
        long chatId = 1L;

        assertThat(linkService.trackNew(chatId, url))
            .isEqualTo(link.getId());

        Mockito.verify(trackRepository).add(Mockito.eq(new TrackRecord(chatId, link.getId())));
    }

    @Test
    @DisplayName("Test trackNew() with not existing Link")
    void trackNew_not_existing_link() {
        String url = "http://some.com";

        Mockito.when(updateService.supports(Mockito.eq(url))).thenReturn(true);
        long linkId = 111L;
        Mockito.when(linkRepository.add(Mockito.any(Link.class))).thenReturn(linkId);
        long chatId = 1L;

        assertThat(linkService.trackNew(chatId, url))
            .isEqualTo(linkId);

        Mockito.verify(trackRepository).add(Mockito.eq(new TrackRecord(chatId, linkId)));
    }

    @Test
    @DisplayName("Test untrack() with only one Chat using desired Link")
    void untrack_one_chat() {
        String url = "http://some.com";
        Link link = new Link(12L, URI.create(url), OffsetDateTime.now());

        Mockito.when(linkRepository.getByUri(url)).thenReturn(link);
        Mockito.doNothing().when(trackRepository).delete(new TrackRecord(11L, link.getId()));
        Mockito.when(chatRepository.getAllByUrl(url)).thenReturn(new LinkedList<>());

        assertThat(linkService.untrack(11L, url))
            .isEqualTo(link.getId());

        Mockito.verify(linkRepository).delete(link.getId());
    }

    @Test
    @DisplayName("Test untrack() with many Chats using desired Link")
    void untrack_many_chats() {
        String url = "http://some.com";
        Link link = new Link(12L, URI.create(url), OffsetDateTime.now());

        Mockito.when(linkRepository.getByUri(url)).thenReturn(link);
        Mockito.when(chatRepository.getAllByUrl(url)).thenReturn(List.of(new Chat(1L), new Chat(2L)));

        assertThat(linkService.untrack(11L, url))
            .isEqualTo(link.getId());

        Mockito.verifyNoMoreInteractions(linkRepository);
    }
}
