package edu.java.scrapper.repository;

import edu.java.scrapper.domain.Chat;
import java.util.List;

public interface ChatRepository {
    void add(Chat chat);

    void delete(Long id);

    List<Chat> getAll();

    List<Chat> getAllByUrl(String uri);
}
