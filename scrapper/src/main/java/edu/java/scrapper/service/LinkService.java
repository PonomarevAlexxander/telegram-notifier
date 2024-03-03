package edu.java.scrapper.service;

import edu.java.scrapper.domain.Link;
import java.util.List;

public interface LinkService {
    List<Link> getAll(Long chatId);

    Link trackNew(Long chatId, String url);

    Link untrack(Long chatId, String url);
}
