package edu.java.bot.service.notifier;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.domain.Chat;
import edu.java.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifierChatService implements ChatService {
    private final ScrapperClient client;

    @Override
    public void addChat(Chat chat) {
        client.registerNewChat(chat.id());
    }

    @Override
    public void deleteChat(Long id) {
        client.deleteChat(id);
    }
}
