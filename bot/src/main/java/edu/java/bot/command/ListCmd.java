package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.LinkService;

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
//        List<URI> links = service.getTracked(update.message().from().id());
        return new SendMessage(update.message().chat().id(), "List is empty :( Try to add something with /track");
    }
}
