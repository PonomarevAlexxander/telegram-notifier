package edu.java.scrapper.service;

import edu.java.scrapper.domain.Chat;
import java.util.List;

public interface ChatService {
    void register(Long chatId);

    void delete(Long chatId);

    List<Chat> getAllByUrl(String uri);
}
