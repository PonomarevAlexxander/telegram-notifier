package edu.java.bot.service.notifier;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.service.CommandService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NotifierCommandService implements CommandService {
    private final List<Command> commands;

    public NotifierCommandService(List<Command> commands) {
        if (commands == null) {
            throw new IllegalArgumentException("commands can't be null");
        }
        this.commands = commands;
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(update.message().chat().id(), "Unsupported command :( Try /help");
    }
}
