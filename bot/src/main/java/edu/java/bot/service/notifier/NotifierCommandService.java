package edu.java.bot.service.notifier;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.exception.ClientExceptionHandler;
import edu.java.bot.service.CommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifierCommandService implements CommandService {
    private final List<Command> commands;
    private final ClientExceptionHandler handler;

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                try {
                    return command.handle(update);
                } catch (RestClientResponseException e) {
                    log.error("Error from HTTP client: {} - {}", e.getStatusCode(), e.getStatusText());
                    return handler.onClientException(update, e);
                }
            }
        }
        return new SendMessage(update.message().chat().id(), "Unsupported command :( Try /help");
    }
}
