package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Chat> rowMapper;

    @Override
    public void add(Chat chat) {
        try {
            jdbcTemplate.update("insert into chat (id) values (?)", chat.getId());
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistException("Chat already exist", e);
        }
    }

    @Override
    public void delete(Long id) {
        int affected = jdbcTemplate.update("delete from chat where id = ?", id);
        if (affected == 0) {
            throw new ResourceNotExistException(String.format("There is no Chat with id %d", id));
        }
    }

    @Override
    public List<Chat> getAll() {
        return jdbcTemplate.query("select * from chat", rowMapper);
    }

    @Override
    public List<Chat> getAllByUrl(String uri) {
        return jdbcTemplate.query(
            "select chat.* from chat where chat.id in "
            + "(select chat_link.chat_id from link join chat_link on chat_link.link_id = link.id where link.url = ?)",
            rowMapper, uri
        );
    }
}
