package edu.java.scrapper.service;

import edu.java.scrapper.dto.LinkDTO;
import java.util.List;

public interface LinkService {
    List<LinkDTO> getAll(Long chatId);

    Long trackNew(Long chatId, String url);

    Long untrack(Long chatId, String url);
}
