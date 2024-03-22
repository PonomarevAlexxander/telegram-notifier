package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.domain.jooq.tables.ChatLink;
import edu.java.scrapper.domain.jooq.tables.records.ChatLinkRecord;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.TrackRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("IllegalIdentifierName")
public class JooqTrackRepository implements TrackRepository {
    private final DSLContext create;

    @Override
    public void add(TrackRecord record) {
        ChatLinkRecord chatLinkRecord = create.newRecord(ChatLink.CHAT_LINK);
        chatLinkRecord.setChatId(record.getChatId());
        chatLinkRecord.setLinkId(record.getLinkId().intValue());
        try {
            chatLinkRecord.store();
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistException(
                String.format("User with chat id %d already tracks link %d", record.getChatId(), record.getLinkId()),
                e
            );
        }
    }

    @Override
    public void delete(TrackRecord record) {
        int affected = create.delete(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.CHAT_ID.eq(record.getChatId())
                .and(ChatLink.CHAT_LINK.LINK_ID.eq(record.getLinkId().intValue())))
            .execute();

        if (affected == 0) {
            throw new ResourceNotExistException(
                String.format(
                    "There is no record with chat id %d and link id %d",
                    record.getChatId(),
                    record.getLinkId()
                )
            );
        }
    }

    @Override
    public List<TrackRecord> getAll() {
        Result<Record> chats = create.select()
            .from(ChatLink.CHAT_LINK)
            .fetch();
        return chats.map(record -> new TrackRecord(
            record.get(ChatLink.CHAT_LINK.CHAT_ID),
            record.get(ChatLink.CHAT_LINK.LINK_ID).longValue()
        ));
    }
}
