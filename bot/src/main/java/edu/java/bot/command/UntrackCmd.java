package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.parser.MarkdownParser;
import edu.java.bot.service.LinkService;
import edu.java.bot.util.LinkValidator;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class UntrackCmd implements Command {
    private final Pattern regex = Pattern.compile("^/untrack +(?<link>[^ ]+)");

    private final String description =
        "delete link from tracking list: /untrack " + MarkdownParser.monospace("<link>");
    private final LinkService service;

    public UntrackCmd(LinkService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        Matcher matcher = regex.matcher(update.message().text());
        if (matcher.find()) {
            String link = matcher.group("link");
            if (!LinkValidator.isValid(link)) {
                return new SendMessage(chatId, "You have sent invalid link");
            }

            URI uri = URI.create(link);
            service.untrackLink(chatId, uri);
            return new SendMessage(chatId, String.format("Link %s was removed from your track list", uri));
        }

        return new SendMessage(chatId, "You have sent invalid command, see /help");
    }
}
