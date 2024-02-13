package edu.java.bot.service.notifier;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.StartCmd;
import edu.java.bot.command.TrackCmd;
import edu.java.bot.service.CommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class NotifierCommandServiceTest {
    @Test
    @DisplayName("Test commands()")
    void commands() {
        List<Command> commands = List.of(new StartCmd(null), new TrackCmd(null));
        CommandService commandService = new NotifierCommandService(commands);
        assertThat(commandService.commands())
            .asList()
            .isEqualTo(commands);
    }

    @Test
    @DisplayName("Test process() with null commands")
    void process_null() {
        assertThatThrownBy(() -> new NotifierCommandService(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Test process() with empty commands")
    void process_empty() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);
        CommandService commandService = new NotifierCommandService(List.of());
        SendMessage msg = commandService.process(update);

        assertThat(msg)
            .isInstanceOf(SendMessage.class);
        assertThat(msg.getParameters())
            .asString()
            .contains("Unsupported command");
    }

    @Test
    @DisplayName("Test process() with unsupported commands")
    void process_unsupported() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().chat().id()).thenReturn(1L);

        Command command = Mockito.mock(Command.class);
        Mockito.when(command.supports(Mockito.any(Update.class))).thenReturn(false);

        CommandService commandService = new NotifierCommandService(List.of(command));
        SendMessage msg = commandService.process(update);

        assertThat(msg)
            .isInstanceOf(SendMessage.class);
        assertThat(msg.getParameters())
            .asString()
            .contains("Unsupported command");
    }

    @Test
    @DisplayName("Test process() with supported commands")
    void process_supported() {
        Update update = Mockito.mock(Update.class);

        Command command = Mockito.mock(Command.class);
        Mockito.when(command.supports(Mockito.any(Update.class))).thenReturn(true);
        Mockito.when(command.handle(Mockito.any(Update.class))).thenReturn(new SendMessage(1L, "well done"));

        CommandService commandService = new NotifierCommandService(List.of(command));
        SendMessage msg = commandService.process(update);

        assertThat(msg)
            .isInstanceOf(SendMessage.class);
        assertThat(msg.getParameters())
            .asString()
            .doesNotContain("Unsupported command");
    }
}
