package edu.java.scrapper.client.github;

import edu.java.scrapper.client.github.dto.Repository;
import java.net.URI;
import java.time.OffsetDateTime;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

class GithubRepositoryUpdateHandlerTest {

    @Test
    @DisplayName("Test getUpdate() with new updates")
    void getUpdate_new_fetched() {
        GithubClient client = Mockito.mock(GithubClient.class);
        Mockito.when(client.getRepository(anyString(), anyString())).thenReturn(new Repository(OffsetDateTime.now()));
        GithubRepositoryUpdateHandler handler = new GithubRepositoryUpdateHandler(client);

        Link link = new Link(1L, URI.create("https://github.com/bronson/mamasterpiece"), OffsetDateTime.MIN);
        LinkUpdate update = handler.getUpdate(link);

        assertThat(update.updated())
            .isTrue();
        assertThat(update.updateInfo())
            .asString()
            .isNotBlank();
        assertThat(update.link())
            .isEqualTo(link);
    }

    @Test
    @DisplayName("Test getUpdate() with no updates")
    void getUpdate_no_fetched() {
        GithubClient client = Mockito.mock(GithubClient.class);
        Mockito.when(client.getRepository(anyString(), anyString())).thenReturn(new Repository(OffsetDateTime.MIN));
        GithubRepositoryUpdateHandler handler = new GithubRepositoryUpdateHandler(client);

        Link link = new Link(1L, URI.create("https://github.com/bronson/mamasterpiece"), OffsetDateTime.now());
        LinkUpdate update = handler.getUpdate(link);

        assertThat(update.updated())
            .isFalse();
        assertThat(update.link())
            .isEqualTo(link);
    }

    @ParameterizedTest
    @CsvSource({
        "https://github.com/bruhlink",
        "https://github.com/"
    })
    @DisplayName("Test getUpdate() with invalid link")
    void getUpdate_with_invalid(URI url) {
        GithubClient client = Mockito.mock(GithubClient.class);
        GithubRepositoryUpdateHandler handler = new GithubRepositoryUpdateHandler(client);

        Link link = new Link(1L, url, OffsetDateTime.now());
        assertThatThrownBy(() -> handler.getUpdate(link))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
        "https://github.com/bruhlink, false",
        "https://github.com/, false",
        "https://github.com/validname/repo, true",
        "https://github.com/validname/re1234po, true",
        "https://github.com/valid-name/rep_s_o, true",
        "https://github.com/N--ot/Valid/repos, false",
        "https://github.com/NotValid/repos/issues, false"
    })
    @DisplayName("Test supports() on different links")
    void supports(URI link, boolean supports) {
        GithubClient client = Mockito.mock(GithubClient.class);
        GithubRepositoryUpdateHandler handler = new GithubRepositoryUpdateHandler(client);

        assertThat(handler.supports(link))
            .isEqualTo(supports);
    }
}
