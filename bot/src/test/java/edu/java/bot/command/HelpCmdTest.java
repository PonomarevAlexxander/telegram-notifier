package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HelpCmdTest {
    List<Command> commands = List.of(Mockito.mock(Command.class));
    Command command = new HelpCmd(commands);

    @Test
    @DisplayName("Test command()")
    void command() {
        assertThat(command.command())
            .isNotBlank()
            .contains("/help");
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
    @DisplayName("Test handle() with return name")
    void handle_valid() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);
        Mockito.when(update.message().from().firstName()).thenReturn("Name");

        assertThat(command.handle(update).getParameters().get("text"))
            .asString()
            .contains("Name");
    }

    @Test
    @DisplayName("Test HelpCmd(List) with null list")
    void init_null() {
        assertThatThrownBy(() -> new HelpCmd(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
