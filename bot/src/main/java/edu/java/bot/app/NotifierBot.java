package edu.java.bot.app;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMyCommands;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.Command;
import edu.java.bot.service.CommandService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotifierBot implements Bot {
    private final TelegramBot bot;
    private final ExceptionHandler handler;
    private final CommandService service;
    private final static Logger LOGGER = LoggerFactory.getLogger(NotifierBot.class);

    public NotifierBot(CommandService service, TelegramBot bot, ExceptionHandler handler) {
        this.bot = bot;
        this.handler = handler;
        this.service = service;
    }

    @PostConstruct
    public void start() {
        LOGGER.info("Bot started...");

        bot.setUpdatesListener(this, handler);
        setCommands();
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        R response = bot.execute(request);
        if (!response.isOk()) {
            LOGGER.error(response.description());
        }
    }

    @Override
    public void close() {
        deleteCommands();
        bot.removeGetUpdatesListener();
        LOGGER.info("Bot stopped...");
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            SendMessage request;
            request = service.process(update);
            execute(request);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void setCommands() {
        BotCommand[] botCommands = service.commands().stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);
        execute(new SetMyCommands(botCommands));
    }

    private void deleteCommands() {
        execute(new DeleteMyCommands());
    }
}
