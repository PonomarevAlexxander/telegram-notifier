package edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperChatService implements ChatService {
//    private final ChatRepository repository;

    @Override
    public void register(Long chatId) {
//        repository.insert(chatId);
    }

    @Override
    public void delete(Long chatId) {
//        repository.insert(chatId);
    }
}
