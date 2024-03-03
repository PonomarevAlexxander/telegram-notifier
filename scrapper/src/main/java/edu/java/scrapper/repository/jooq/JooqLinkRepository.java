package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.jooq.tables.ChatLink;
import edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.Link.LINK;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("IllegalIdentifierName")
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext create;
    private final RecordMapper<Record, Link> mapper = record -> new Link(
        record.get(LINK.ID).longValue(),
        URI.create(record.get(LINK.URL)),
        record.get(LINK.LAST_TRACKED)
    );

    @Override
    public Long add(Link link) {
        LinkRecord linkRecord = create.newRecord(LINK);
        linkRecord.setUrl(link.getUri().toString());
        linkRecord.setLastTracked(link.getLastTracked());
        linkRecord.store();
        return linkRecord.getId().longValue();
    }

    @Override
    public void delete(Long id) {
        int affected = create.delete(LINK)
            .where(LINK.ID.eq(Math.toIntExact(id)))
            .execute();

        if (affected == 0) {
            throw new ResourceNotExistException(String.format("There is no Link with id %d", id));
        }
    }

    @Override
    public Link getByUri(String uri) {
        return create.fetchOne(LINK, LINK.URL.eq(uri))
            .map(mapper);
    }

    @Override
    public List<Link> getAll() {
        Result<Record> links = create.select()
            .from(LINK)
            .fetch();
        return links.map(mapper);
    }

    @Override
    public List<Link> getAllLByChatId(Long chatId) {
        Result<Record> links = create.select()
            .from(LINK)
            .join(ChatLink.CHAT_LINK)
            .on(ChatLink.CHAT_LINK.LINK_ID.eq(LINK.ID))
            .where(ChatLink.CHAT_LINK.CHAT_ID.eq(chatId))
            .fetch();
        return links.map(mapper);
    }

    @Override
    public List<Link> getAllBefore(OffsetDateTime time) {
        Result<Record> links = create.select()
            .from(LINK)
            .where(LINK.LAST_TRACKED.lessThan(time))
            .fetch();
        return links.map(mapper);
    }

    @Override
    public void updateLastTrackedTime(List<Long> linkIds, OffsetDateTime time) {
        create.update(LINK)
            .set(LINK.LAST_TRACKED, time)
            .where(LINK.ID.in(linkIds))
            .execute();
    }
}
