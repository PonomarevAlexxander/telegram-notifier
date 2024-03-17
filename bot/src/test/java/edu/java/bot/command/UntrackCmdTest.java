package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.LinkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UntrackCmdTest {
    @Mock
    LinkService service;

    @Test
    @DisplayName("Test command()")
    void command() {
        Command command = new UntrackCmd(service);
        assertThat(command.command())
            .isNotBlank()
            .contains("/untrack");
    }

    @Test
    @DisplayName("Test description()")
    void description() {
        Command command = new UntrackCmd(service);
        assertThat(command.description())
            .isNotBlank();
    }

    @Test
    @DisplayName("Test handle() on used chat_id")
    void handle() {
        Command command = new UntrackCmd(service);
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);
        Mockito.when(update.message().text()).thenReturn("/untrack invalidlink");
        assertThat(command.handle(update).getParameters().get("chat_id"))
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("Test handle() with invalid link")
    void handle_invalid() {
        Command command = new UntrackCmd(service);
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);
        Mockito.when(update.message().text()).thenReturn("/untrack invalidlink");

        assertThat(command.handle(update).getParameters().get("text"))
            .asString()
            .contains("invalid");
    }
}
