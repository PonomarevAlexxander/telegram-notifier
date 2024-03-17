package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class JdbcChatRepositoryTest extends IntegrationTest {
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
    void add_no_error() {
        Chat chat = new Chat(1L);
        chatRepository.add(chat);

        List<Chat> chats = chatRepository.getAll();
        assertThat(chats)
            .asList()
            .containsExactly(chat);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test add() on duplicates")
    void add_error() {
        Chat chat = new Chat(1L);
        chatRepository.add(chat);

        assertThatThrownBy(() -> chatRepository.add(chat))
            .isInstanceOf(ResourceAlreadyExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with no error")
    void delete_no_error() {
        Chat chat = new Chat(1L);
        chatRepository.add(chat);
        chatRepository.delete(1L);

        assertThat(chatRepository.getAll())
            .asList()
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with not existing id")
    void delete_with_error() {
        assertThatThrownBy(() -> chatRepository.delete(1L))
            .isInstanceOf(ResourceNotExistException.class);
    }


    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAll()")
    void getAll() {
        chatRepository.add(new Chat(1L));
        chatRepository.add(new Chat(2L));
        chatRepository.add(new Chat(3L));

        assertThat(chatRepository.getAll())
            .asList()
            .hasSize(3);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAllByUrl()")
    void getAllByUrl() {
        chatRepository.add(new Chat(1L));
        chatRepository.add(new Chat(2L));
        chatRepository.add(new Chat(3L));

        Long id1 = linkRepository.add(new Link(null, URI.create("https://somesite.com"), OffsetDateTime.now()));
        Long id2 = linkRepository.add(new Link(null, URI.create("https://another.com"), OffsetDateTime.now()));

        trackRepository.add(new TrackRecord(1L, id1));
        trackRepository.add(new TrackRecord(1L, id2));
        trackRepository.add(new TrackRecord(2L, id1));
        trackRepository.add(new TrackRecord(3L, id2));

        List<Chat> chats = chatRepository.getAllByUrl("https://somesite.com");
        assertThat(chats)
            .asList()
            .containsExactly(new Chat(1L), new Chat(2L));

        chats = chatRepository.getAllByUrl("https://another.com");
        assertThat(chats)
            .asList()
            .containsExactly(new Chat(1L), new Chat(3L));
    }
}
