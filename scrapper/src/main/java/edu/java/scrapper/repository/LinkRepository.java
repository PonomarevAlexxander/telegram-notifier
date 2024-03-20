package edu.java.scrapper.repository;

import edu.java.scrapper.domain.Link;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Repository that operates with tracked links
 *
 * @author Alexander Ponomarev
 */
public interface LinkRepository {
    /**
     * Adds new link
     * @return new link id if created
     */
    Long add(Link link);

    void delete(Long id);

    Link getByUri(String uri);

    List<Link> getAll();

    List<Link> getAllLByChatId(Long chatId);

    List<Link> getAllBefore(OffsetDateTime time);

    void updateLastTrackedTime(List<Long> linkIds, OffsetDateTime time);
}
