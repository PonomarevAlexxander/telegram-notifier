package edu.java.scrapper.service.impl;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jpa.Link;
import edu.java.scrapper.exception.LinkIsNotSupportedException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
@TestPropertySource(properties = "app.database-access-type=jpa")
@DirtiesContext
class JpaLinkServiceTest extends IntegrationTest {
    @Autowired
    JpaLinkService linkService;
    @Autowired
    JpaLinkRepository linkRepository;
    @Autowired
    JpaChatService chatService;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAll() with no error")
    void getAll() {
        chatService.register(1L);
        chatService.register(2L);
        chatService.register(3L);

        linkService.trackNew(1L, "https://github.com/repo/name");
        linkService.trackNew(1L, "https://github.com/repo/another");
        linkService.trackNew(2L, "https://github.com/repo/name");
        linkService.trackNew(3L, "https://github.com/repo/another");

        assertThat(linkService.getAllByChatId(1L))
            .asList()
            .hasSize(2);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test trackNew() with no error")
    void trackNew_no_err() {
        chatService.register(1L);
        linkService.trackNew(1L, "https://github.com/repo/name");

        assertThat(linkService.getAllByChatId(1L))
            .asList()
            .hasSize(1);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test trackNew() with not existing chat")
    void trackNew_with_err_1() {
       assertThatThrownBy(() -> linkService.trackNew(1L, "https://github.com/repo/name"))
           .isInstanceOf(ResourceNotExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test trackNew() with not supported link")
    void trackNew_with_err_2() {
        assertThatThrownBy(() -> linkService.trackNew(1L, "https://some.com/not/supported"))
            .isInstanceOf(LinkIsNotSupportedException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test untrack() with no error")
    void untrack() {
        chatService.register(1L);

        linkService.trackNew(1L, "https://github.com/repo/name");
        linkService.trackNew(1L, "https://github.com/repo/another");

        linkService.untrack(1L, "https://github.com/repo/name");
        linkService.untrack(1L, "https://github.com/repo/another");

        assertThat(linkService.getAllByChatId(1L))
            .asList()
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test untrack() with not existing link")
    void untrack_with_err_1() {
        chatService.register(2L);
        assertThatThrownBy(() -> linkService.untrack(1L, "https://github.com/repo/name"))
            .isInstanceOf(ResourceNotExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test untrack() with not existing chat")
    void untrack_with_err_2() {
        chatService.register(1L);
        linkService.trackNew(1L, "https://github.com/repo/name");

        assertThatThrownBy(() -> linkService.untrack(2L, "https://github.com/repo/name"))
            .isInstanceOf(ResourceNotExistException.class);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAllBefore()")
    void getAllBefore() {
        Link link1 = new Link();
        link1.setUri("https://localhost/some");
        OffsetDateTime now = OffsetDateTime.now();
        link1.setLastTracked(now.minusMinutes(30));
        link1 = linkRepository.save(link1);

        Link link2 = new Link();
        link2.setUri("https://localhost/someqqq");
        link2.setLastTracked(now.minusMinutes(12));
        link2 = linkRepository.save(link2);

        Link link3 = new Link();
        link3.setUri("https://localhost/someqq1212q");
        link3.setLastTracked(now.minusMinutes(15));
        link3 = linkRepository.save(link3);

        assertThat(linkService.getAllBefore(now.minusMinutes(12)))
            .asList()
            .hasSize(2);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test updateLastTrackedTime()")
    void updateLastTrackedTime() {
        chatService.register(1L);
        chatService.register(2L);
        chatService.register(3L);

        List<Long> ids = new ArrayList<>();

        ids.add(linkService.trackNew(1L, "https://github.com/repo/name"));
        ids.add(linkService.trackNew(1L, "https://github.com/repo/onemore"));
        linkService.trackNew(2L, "https://github.com/repo/name");
        linkService.trackNew(3L, "https://github.com/repo/another");

        OffsetDateTime time = OffsetDateTime.parse("2022-10-29T10:00:00+03:00");
        linkService.updateLastTrackedTime(ids, time);
        assertThat(linkRepository.getLinkByUri("https://github.com/repo/name").get().getLastTracked())
            .isEqualTo(time);
        assertThat(linkRepository.getLinkByUri("https://github.com/repo/onemore").get().getLastTracked())
            .isEqualTo(time);
    }
}
