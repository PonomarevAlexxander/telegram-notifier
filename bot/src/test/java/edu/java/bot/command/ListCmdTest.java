package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ListCmdTest {
    @Mock
    LinkService service;

    @Test
    @DisplayName("Test command()")
    void command() {
        Command command = new ListCmd(service);
        assertThat(command.command())
            .isNotBlank()
            .contains("/list");
    }

    @Test
    @DisplayName("Test description()")
    void description() {
        Command command = new ListCmd(service);
        assertThat(command.description())
            .isNotBlank();
    }

    @Test
    @DisplayName("Test handle() on used chat_id")
    void handle_chat() {
        Command command = new ListCmd(service);
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);
        Mockito.when(service.getTracked(Mockito.anyLong())).thenReturn(List.of(URI.create("https://github.com")));

        assertThat(command.handle(update).getParameters().get("chat_id"))
            .isEqualTo(1L);
    }
}
