package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "app.database-access-type=jdbc")
class JdbcTrackRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcTrackRepository trackRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test add() with no error")
    void add_no_err() {
        Long id = linkRepository.add(new Link(null, URI.create("http://some.com"), OffsetDateTime.now()));
        chatRepository.add(new Chat(1L));

        TrackRecord record = new TrackRecord(1L, id);
        trackRepository.add(record);

        assertThat(trackRepository.getAll())
            .asList()
            .containsExactly(record);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test add() on duplicates")
    void add_with_err_1() {
        chatRepository.add(new Chat(1L));
        chatRepository.add(new Chat(2L));

        Long id1 = linkRepository.add(new Link(null, URI.create("https://somesite.com"), OffsetDateTime.now()));
        Long id2 = linkRepository.add(new Link(null, URI.create("https://another.com"), OffsetDateTime.now()));

        trackRepository.add(new TrackRecord(1L, id1));
        trackRepository.add(new TrackRecord(1L, id2));
        trackRepository.add(new TrackRecord(2L, id1));

        assertThatThrownBy(() -> trackRepository.add(new TrackRecord(1L, id1)))
            .isInstanceOf(ResourceAlreadyExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test add() with not existing chat")
    void add_with_err_2() {
        chatRepository.add(new Chat(2L));

        Long id1 = linkRepository.add(new Link(null, URI.create("https://somesite.com"), OffsetDateTime.now()));

        assertThatThrownBy(() ->  trackRepository.add(new TrackRecord(1L, id1)))
            .isInstanceOf(ResourceNotExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with no error")
    void delete_no_err() {
        Long id = linkRepository.add(new Link(null, URI.create("http://some.com"), OffsetDateTime.now()));
        chatRepository.add(new Chat(1L));

        TrackRecord record = new TrackRecord(1L, id);
        trackRepository.add(record);
        trackRepository.delete(record);

        assertThat(trackRepository.getAll())
            .asList()
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with not existing record")
    void delete_with_err() {
        assertThatThrownBy(() -> trackRepository.delete(new TrackRecord(1L, 1L)));
    }
}
