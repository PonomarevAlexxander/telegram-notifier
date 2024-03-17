package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.domain.TrackRecord;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.TrackRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("IllegalIdentifierName")
public class JdbcTrackRepository implements TrackRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(TrackRecord record) {
        try {
            jdbcTemplate.update("insert into chat_link (chat_id, link_id) values (?, ?)",
                record.getChatId(), record.getLinkId()
            );
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistException(
                String.format("User with chat id %d already tracks link %d", record.getChatId(), record.getLinkId()),
                e
            );
        }
    }

    @Override
    public void delete(TrackRecord record) {
        int affected = jdbcTemplate.update("delete from chat_link where chat_id = ? and link_id = ?",
            record.getChatId(), record.getLinkId()
        );
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
        return jdbcTemplate.query(
            "select * from chat_link",
            (rs, rowNum) -> new TrackRecord(rs.getLong(1), rs.getLong(2))
        );
    }
}
