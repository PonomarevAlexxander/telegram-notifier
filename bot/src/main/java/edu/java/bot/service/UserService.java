package edu.java.bot.service;

import edu.java.bot.domain.User;

public interface UserService {
    void addUser(User user);

    void deleteUser(Long id);
}
