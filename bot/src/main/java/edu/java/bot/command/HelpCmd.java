package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.parser.MarkdownParser;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HelpCmd implements Command {
    private final List<Command> commands;

    public HelpCmd(List<Command> commands) {
        if (commands == null) {
            throw new IllegalArgumentException("commands can't be null");
        }

        this.commands = commands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "lists all available commands";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder builder = new StringBuilder();
        builder.append("Hi, ").append(update.message().from().firstName()).append("\\!\n");
        builder.append("This is the list of available commands:\n");
        var commandIt = commands.iterator();
        while (commandIt.hasNext()) {
            var command = commandIt.next();
            builder.append("• ");
            builder.append(command.command());
            builder.append(' ').append('\\').append('-').append(' ');
            builder.append(MarkdownParser.italic(command.description()));
            if (commandIt.hasNext()) {
                builder.append('\n');
            }
        }

        return new SendMessage(update.message().chat().id(), builder.toString())
            .parseMode(ParseMode.MarkdownV2);
    }
}
