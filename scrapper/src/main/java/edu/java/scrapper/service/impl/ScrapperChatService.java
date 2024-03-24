package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ScrapperChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void register(Long chatId) {
        chatRepository.add(new Chat(chatId));
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        chatRepository.delete(chatId);
    }

    @Override
    public List<Chat> getAllByUrl(String uri) {
        return chatRepository.getAllByUrl(uri);
    }
}
