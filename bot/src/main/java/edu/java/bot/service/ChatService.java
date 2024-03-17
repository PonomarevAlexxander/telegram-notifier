package edu.java.bot.service;

import edu.java.bot.domain.Chat;

public interface ChatService {
    void addChat(Chat chat);

    void deleteChat(Long id);
}
