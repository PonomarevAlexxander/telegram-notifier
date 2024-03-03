package edu.java.bot.repository;

import edu.java.bot.domain.User;

public interface UserRepository {
    void insertUser(User user);

    void deleteUser(Long id);

    boolean isRegistered(Long chatId);
}
