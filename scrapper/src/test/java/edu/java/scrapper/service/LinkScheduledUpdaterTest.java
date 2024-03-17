package edu.java.scrapper.service;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.LinkUpdateRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LinkScheduledUpdaterTest {
    @Mock
    private UpdateService updateService;
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationConfig config;
    @Mock
    private BotClient botClient;

    @InjectMocks
    private LinkScheduledUpdater linkScheduledUpdater;

    @Test
    @DisplayName("Test update() with no updates")
    void update_nothing() {
        List<Link> links = List.of(
            new Link(1L, URI.create("http://some.com"), OffsetDateTime.now().minusMinutes(30)),
            new Link(2L, URI.create("http://another.com"), OffsetDateTime.now().minusMinutes(30)),
            new Link(3L, URI.create("http://link.com"), OffsetDateTime.now().minusMinutes(30))
        );

        Mockito.when(config.scheduler().forceCheckDelay()).thenReturn(Duration.of(30, ChronoUnit.MINUTES));
        Mockito.when(linkRepository.getAllBefore(Mockito.any(OffsetDateTime.class))).thenReturn(links);
        Mockito.when(updateService.fetchUpdates(Mockito.anyList())).thenReturn(List.of());

        linkScheduledUpdater.update();

        Mockito.verifyNoInteractions(chatRepository, botClient);
        Mockito.verify(linkRepository).updateLastTrackedTime(Mockito.anyList(), Mockito.any(OffsetDateTime.class));
    }

    @Test
    @DisplayName("Test update() some updates")
    void update_something() {
        List<Link> links = List.of(
            new Link(1L, URI.create("http://some.com"), OffsetDateTime.now().minusMinutes(30)),
            new Link(2L, URI.create("http://another.com"), OffsetDateTime.now().minusMinutes(30)),
            new Link(3L, URI.create("http://link.com"), OffsetDateTime.now().minusMinutes(30))
        );

        Mockito.when(config.scheduler().forceCheckDelay()).thenReturn(Duration.of(30, ChronoUnit.MINUTES));
        Mockito.when(linkRepository.getAllBefore(Mockito.any(OffsetDateTime.class))).thenReturn(links);
        Mockito.when(updateService.fetchUpdates(Mockito.anyList())).
            thenReturn(List.of(
                new LinkUpdate(links.get(0), true, "sth"),
                new LinkUpdate(links.get(1), true, "sth")
            ));
        Mockito.when(chatRepository.getAllByUrl(Mockito.any())).thenReturn(List.of(new Chat(1L), new Chat(2L)));

        linkScheduledUpdater.update();

        Mockito.verify(chatRepository, Mockito.times(2)).getAllByUrl(Mockito.any());
        Mockito.verify(botClient, Mockito.times(2)).sendUpdatesOnLink(Mockito.any(LinkUpdateRequest.class));
        Mockito.verify(linkRepository).updateLastTrackedTime(Mockito.anyList(), Mockito.any(OffsetDateTime.class));
    }
}
