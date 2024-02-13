package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StartCmdTest {
    @Mock UserService service;
    Command command = new StartCmd(service);

    @Test
    @DisplayName("Test command()")
    void command() {
        assertThat(command.command())
            .isNotBlank()
            .contains("/start");
    }

    @Test
    @DisplayName("Test description()")
    void description() {
        assertThat(command.description())
            .isNotBlank();
    }

    @Test
    @DisplayName("Test handle() on used chat_id")
    void handle_chat() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);
        assertThat(command.handle(update).getParameters().get("chat_id"))
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("Test handle()")
    void handle_valid() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);

        assertThat(command.handle(update).getParameters().get("text"))
            .asString()
            .contains("successfully started");
    }
}
