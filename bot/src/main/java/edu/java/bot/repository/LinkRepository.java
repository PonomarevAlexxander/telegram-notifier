package edu.java.bot.repository;

import edu.java.bot.domain.Link;

public interface LinkRepository {
    void insertLink(Link link);

    void deleteLink(Long id);
}
