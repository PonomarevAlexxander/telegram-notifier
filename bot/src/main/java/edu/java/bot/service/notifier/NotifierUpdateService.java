package edu.java.bot.service.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.LinkUpdate;
import edu.java.bot.service.UpdateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifierUpdateService implements UpdateService {
    private final TelegramBot bot;

    @Override
    public void processUpdates(LinkUpdate update, List<Long> chatIds) {
        String msg = "Link " + update.url().toString() + " was updated:\n" + update.description();
        for (var id : chatIds) {
            var result = bot.execute(new SendMessage(id, msg));
            if (!result.isOk()) {
                log.error(
                    "Failed to sent update due to: {} {}",
                    result.errorCode(),
                    result.description()
                );
            }
        }
    }
}
