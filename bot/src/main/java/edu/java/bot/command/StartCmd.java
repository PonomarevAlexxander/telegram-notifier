package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.Chat;
import edu.java.bot.parser.MarkdownParser;
import edu.java.bot.service.ChatService;
import org.springframework.stereotype.Component;

@Component
public class StartCmd implements Command {
    private final ChatService service;
    private final String answer =
        "You have successfully started using " + MarkdownParser.bold("Notify bot") + "\\! Enjoy it\\!";

    public StartCmd(ChatService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "start working with bot";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        service.addChat(new Chat(id));
        return new SendMessage(
            id,
            answer
            ).parseMode(ParseMode.MarkdownV2);
    }
}
