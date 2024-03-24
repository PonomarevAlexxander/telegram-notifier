package edu.java.scrapper.service;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.dto.LinkDTO;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    List<LinkDTO> getAllByChatId(Long chatId);

    Long trackNew(Long chatId, String url);

    Long untrack(Long chatId, String url);

    List<Link> getAllBefore(OffsetDateTime time);

    void updateLastTrackedTime(List<Long> ids, OffsetDateTime time);
}
