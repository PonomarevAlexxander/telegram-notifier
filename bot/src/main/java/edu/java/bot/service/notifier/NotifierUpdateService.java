package edu.java.bot.service.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.LinkUpdate;
import edu.java.bot.service.UpdateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifierUpdateService implements UpdateService {
    private final TelegramBot bot;

    @Override
    public void processUpdates(LinkUpdate update, List<Long> chatIds) {
        String msg = "Resource " + update.url() + " was updated:\n" + update.description();
        for (var id : chatIds) {
            bot.execute(new SendMessage(id, msg));
        }
    }
}
