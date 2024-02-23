package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.stackoverflow.dto.StackOverflowAnswer;
import edu.java.scrapper.client.stackoverflow.dto.StackOverflowAnswersResponse;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;

class StackOverflowQuestionHandlerTest {

    @Test
    @DisplayName("Test getUpdate() with new updates")
    void getUpdate_new_fetched() {
        StackOverflowClient client = Mockito.mock(StackOverflowClient.class);
        Mockito.when(client.getAllByQuestionFromDate(anyLong(), anyLong())).thenReturn(new StackOverflowAnswersResponse(
            List.of(new StackOverflowAnswer(OffsetDateTime.now()), new StackOverflowAnswer(OffsetDateTime.now()))));
        StackOverflowQuestionHandler handler = new StackOverflowQuestionHandler(client);

        Link link = new Link(
            1L,
            URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c"),
            OffsetDateTime.now()
        );
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
        StackOverflowClient client = Mockito.mock(StackOverflowClient.class);
        Mockito.when(client.getAllByQuestionFromDate(anyLong(), anyLong())).thenReturn(new StackOverflowAnswersResponse(
            List.of()));
        StackOverflowQuestionHandler handler = new StackOverflowQuestionHandler(client);

        Link link = new Link(
            1L,
            URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c"),
            OffsetDateTime.now()
        );
        LinkUpdate update = handler.getUpdate(link);

        assertThat(update.updated())
            .isFalse();
        assertThat(update.link())
            .isEqualTo(link);
    }

    @ParameterizedTest
    @CsvSource({
        "https://github.com/validname/repo",
        "https://stackoverflow.com/questions/12342"
    })
    @DisplayName("Test getUpdate() with invalid link")
    void getUpdate_with_invalid(URI url) {
        StackOverflowClient client = Mockito.mock(StackOverflowClient.class);
        StackOverflowQuestionHandler handler = new StackOverflowQuestionHandler(client);

        Link link = new Link(1L, url, OffsetDateTime.now());
        assertThatThrownBy(() -> handler.getUpdate(link))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c, true",
        "https://stackoverflow.com/questions/12342/some-other-kebab, true",
        "https://github.com/validname/repo, false",
        "https://stackoverflow.com/questions/12342, false",
        "https://stackoverflow.com/questions/aaa/some-other-kebab, false",
        "https://stackoverflow.com/answers/some-other-kebab, false",
    })
    @DisplayName("Test supports() on different links")
    void supports(URI link, boolean supports) {
        StackOverflowClient client = Mockito.mock(StackOverflowClient.class);
        StackOverflowQuestionHandler handler = new StackOverflowQuestionHandler(client);

        assertThat(handler.supports(link.toString()))
            .isEqualTo(supports);
    }
}
