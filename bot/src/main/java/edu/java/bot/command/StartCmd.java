package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.parser.MarkdownParser;
import edu.java.bot.service.UserService;

public class StartCmd implements Command {
    private final UserService service;
    private final String answer =
        "You have successfully started using " + MarkdownParser.bold("Notify bot") + "\\! Enjoy it\\!";

    public StartCmd(UserService service) {
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
        var fromUser = update.message().from();
//        service.addUser(new User(fromUser.id(), update.message().chat().id(), fromUser.firstName()));
        return new SendMessage(
            update.message().chat().id(),
            answer
            ).parseMode(ParseMode.MarkdownV2);
    }
}
