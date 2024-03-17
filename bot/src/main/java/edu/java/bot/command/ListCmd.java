package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListCmd implements Command {
    private final LinkService service;

    public ListCmd(LinkService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "shows list of all tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        List<URI> links = service.getTracked(update.message().from().id());
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), "List is empty :( Try to add something with /track");
        }

        StringBuilder builder = new StringBuilder("Here is your links list:\n");
        for (var uri : links) {
            builder.append("- ");
            builder.append(uri.toString());
            builder.append('\n');
        }
        return new SendMessage(update.message().chat().id(), builder.toString());
    }
}
