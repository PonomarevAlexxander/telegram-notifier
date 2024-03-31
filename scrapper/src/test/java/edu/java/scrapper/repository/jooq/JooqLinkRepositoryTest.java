package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "app.database-access-type=jooq")
@DirtiesContext
class JooqLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqTrackRepository trackRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test add() with no error")
    void add_no_error() {
        OffsetDateTime time = OffsetDateTime.parse("2022-10-29T10:00:00+03:00");
        Link link = new Link(null, URI.create("https://somesite.com"), time);
        Long id = linkRepository.add(link);

        assertThat(linkRepository.getAll().getFirst().getId())
            .isEqualTo(id);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test add() on duplicate")
    void add_with_error() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        Link link = new Link(null, URI.create("https://somesite.com"), now);
        linkRepository.add(link);

        assertThatThrownBy(() -> linkRepository.add(link))
            .isInstanceOf(ResourceAlreadyExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with no error")
    void delete_no_error() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        Link link = new Link(null, URI.create("https://somesite.com"), now);
        Long id = linkRepository.add(link);
        linkRepository.delete(id);

        assertThat(chatRepository.getAll())
            .asList()
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with not existing id")
    void delete_with_error() {
        assertThatThrownBy(() -> linkRepository.delete(1L))
            .isInstanceOf(ResourceNotExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getByUri() with no error")
    void getByUri_no_err() {
        OffsetDateTime time = OffsetDateTime.parse("2022-10-29T10:00:00+03:00");
        Link link = new Link(null, URI.create("https://somesite.com"), time);
        Long id = linkRepository.add(link);

        assertThat(linkRepository.getByUri("https://somesite.com").getId())
            .isEqualTo(id);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getByUri() with not existing uri")
    void getByUri_with_err() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        Link link = new Link(null, URI.create("https://somesite.com"), now);
        linkRepository.add(link);

        assertThatThrownBy(() -> linkRepository.getByUri("https://site.com"))
            .isInstanceOf(ResourceNotExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAll()")
    void getAll() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        linkRepository.add(new Link(null, URI.create("https://someeeesite.com"), now));
        linkRepository.add(new Link(null, URI.create("https://somesite.com"), now));
        linkRepository.add(new Link(null, URI.create("https://site.com"), now));

        assertThat(linkRepository.getAll())
            .asList()
            .hasSize(3);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAllLByChatId()")
    void getAllLByChatId() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        Long id1 = linkRepository.add(new Link(null, URI.create("https://someeeesite.com"), now));
        Long id2 = linkRepository.add(new Link(null, URI.create("https://somesite.com"), now));
        Long id3 = linkRepository.add(new Link(null, URI.create("https://site.com"), now));

        chatRepository.add(new Chat(1L));
        chatRepository.add(new Chat(2L));
        chatRepository.add(new Chat(3L));

        trackRepository.add(new TrackRecord(1L, id1));
        trackRepository.add(new TrackRecord(1L, id2));
        trackRepository.add(new TrackRecord(1L, id3));
        trackRepository.add(new TrackRecord(2L, id1));
        trackRepository.add(new TrackRecord(3L, id2));
        trackRepository.add(new TrackRecord(3L, id3));

        assertThat(linkRepository.getAllLByChatId(1L))
            .asList()
            .hasSize(3);
        assertThat(linkRepository.getAllLByChatId(2L))
            .asList()
            .hasSize(1);
        assertThat(linkRepository.getAllLByChatId(3L))
            .asList()
            .hasSize(2);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAllLByChatId()")
    void getAllBefore() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        linkRepository.add(new Link(null, URI.create("https://someeeesite.com"), now));
        linkRepository.add(new Link(null, URI.create("https://somesite.com"), now.minusSeconds(123)));
        linkRepository.add(new Link(null, URI.create("https://somesit4e.com"), now.minusMinutes(10)));
        linkRepository.add(new Link(null, URI.create("https://somesi44te.com"), now.minusMinutes(12)));
        linkRepository.add(new Link(null, URI.create("https://somes4444ite.com"), now.minusHours(6)));
        linkRepository.add(new Link(null, URI.create("https://site.com"), now.minusDays(1)));

        assertThat(linkRepository.getAllBefore(now.minusMinutes(10)))
            .asList()
            .hasSize(3);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test updateLastTrackedTime()")
    void updateLastTrackedTime() {
        List<Long> ids = new LinkedList<>();
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
        ids.add(linkRepository.add(new Link(null, URI.create("https://someeeesite.com"), now)));
        linkRepository.add(new Link(null, URI.create("https://somesite.com"), now.minusSeconds(123)));
        ids.add(linkRepository.add(new Link(null, URI.create("https://somesit4e.com"), now.minusMinutes(10))));

        OffsetDateTime newTime = OffsetDateTime.parse("2022-10-29T10:00:00+00");
        linkRepository.updateLastTrackedTime(ids, newTime);

        assertThat(linkRepository.getByUri("https://someeeesite.com").getLastTracked())
            .isEqualTo(newTime);
        assertThat(linkRepository.getByUri("https://somesit4e.com").getLastTracked())
            .isEqualTo(newTime);
    }
}
