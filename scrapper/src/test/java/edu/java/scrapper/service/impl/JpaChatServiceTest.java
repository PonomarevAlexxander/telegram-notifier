package edu.java.scrapper.service.impl;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(properties = "app.database-access-type=jpa")
@DirtiesContext
class JpaChatServiceTest extends IntegrationTest {
    @Autowired
    JpaChatService chatService;
    @Autowired
    JpaChatRepository chatRepository;
    @Autowired
    JpaLinkService linkService;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test register() with no error")
    void register_no_err() {
        chatService.register(1L);
        chatService.register(2L);

        assertThat(chatRepository.findAll())
            .asList()
            .hasSize(2);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test delete() with no error")
    void delete_no_err() {
        chatService.register(1L);
        chatService.delete(1L);

        assertThat(chatRepository.findAll())
            .asList()
            .hasSize(0);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test getAllByUrl() with no error")
    void getAllByUrl() {
        chatService.register(1L);
        chatService.register(2L);
        chatService.register(3L);

        linkService.trackNew(1L, "https://github.com/repo/name");
        linkService.trackNew(2L, "https://github.com/repo/name");
        linkService.trackNew(3L, "https://github.com/repo/another");

        assertThat(chatService.getAllByUrl("https://github.com/repo/name"))
            .asList()
            .hasSize(2);
    }
}
