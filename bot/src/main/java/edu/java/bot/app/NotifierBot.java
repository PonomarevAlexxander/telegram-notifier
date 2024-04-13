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
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifierBot implements Bot {
    private final TelegramBot bot;
    private final ExceptionHandler handler;
    private final CommandService service;
    private final Counter successCounter;
    private final Counter failCounter;

    @SuppressWarnings("MultipleStringLiterals")
    public NotifierBot(CommandService service, TelegramBot bot, ExceptionHandler handler, MeterRegistry registry) {
        this.bot = bot;
        this.handler = handler;
        this.service = service;
        this.successCounter = Counter.builder("telegram.messages.processed")
            .tags("type", "success")
            .register(registry);
        this.failCounter = Counter.builder("telegram.messages.processed")
            .tags("type", "failed")
            .register(registry);
    }

    @PostConstruct
    public void start() {
        log.info("Bot started...");

        bot.setUpdatesListener(this, handler);
        setCommands();
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        R response = bot.execute(request);
        if (!response.isOk()) {
            log.error(response.description());
        }
    }

    @Override
    public void close() {
        deleteCommands();
        bot.removeGetUpdatesListener();
        log.info("Bot stopped...");
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            SendMessage request;
            try {
                request = service.process(update);
                successCounter.increment();
            } catch (Exception ex) {
                log.error("Error: {}", ex.toString());
                request = new SendMessage(
                    update.message().chat().id(),
                    "Service is currently unavailable or some internal error happened."
                );
                failCounter.increment();
            }
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
