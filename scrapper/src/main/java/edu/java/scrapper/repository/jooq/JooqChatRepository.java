package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.jooq.tables.ChatLink;
import edu.java.scrapper.domain.jooq.tables.Link;
import edu.java.scrapper.domain.jooq.tables.records.ChatRecord;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("IllegalIdentifierName")
public class JooqChatRepository implements ChatRepository {
    private final DSLContext create;

    @Override
    public void add(Chat chat) {
        ChatRecord chatRecord = create.newRecord(CHAT);
        chatRecord.setId(chat.getId());
        try {
            chatRecord.store();
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistException(String.format("Chat %d already exist", chat.getId()), e);
        }
    }

    @Override
    public void delete(Long id) {
        int affected = create.delete(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();

        if (affected == 0) {
            throw new ResourceNotExistException(String.format("There is no Chat with id %d", id));
        }
    }

    @Override
    public List<Chat> getAll() {
        Result<Record> chats = create.select()
            .from(CHAT)
            .fetch();
        return chats.map(record -> new Chat(record.get(CHAT.ID)));
    }

    @Override
    public List<Chat> getAllByUrl(String uri) {
        Result<Record> chats = create.select()
            .from(CHAT)
            .where(CHAT.ID.in(create.select(ChatLink.CHAT_LINK.CHAT_ID)
                .from(Link.LINK)
                .join(ChatLink.CHAT_LINK)
                .on(ChatLink.CHAT_LINK.LINK_ID.eq(Link.LINK.ID))
                .where(Link.LINK.URL.eq(uri))))
            .fetch();
        return chats.map(record -> new Chat(record.get(CHAT.ID)));
    }
}
