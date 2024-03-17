package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
}
