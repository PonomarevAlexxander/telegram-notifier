package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.LinkRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> rowMapper;

    @Override
    public Long add(Link link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                    .prepareStatement(
                        "insert into link (url, last_tracked) values (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                    );
                ps.setString(1, link.getUri().toString());
                ps.setObject(2, link.getLastTracked());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistException(String.format(
                "Link with url '%s' already exist",
                link.getUri().toString()
            ), e);
        }

        Integer id = (Integer) keyHolder.getKeys().get("id");
        return id.longValue();
    }

    @Override
    public void delete(Long id) {
        int affected = jdbcTemplate.update("delete from link where id = ?", id);
        if (affected == 0) {
            throw new ResourceNotExistException(String.format("There is no Link with id %d", id));
        }
    }

    @Override
    public Link getByUri(String uri) {
        try {
            return jdbcTemplate.queryForObject("select * from link where url = ?", rowMapper, uri);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotExistException(String.format("There is no Link with uri '%s'", uri), e);
        }
    }

    @Override
    public List<Link> getAll() {
        return jdbcTemplate.query("select * from link", rowMapper);
    }

    @Override
    public List<Link> getAllLByChatId(Long chatId) {
        return jdbcTemplate.query(
            "select link.* from link join chat_link on link.id = chat_link.link_id where chat_link.chat_id = ?",
            rowMapper, chatId
        );
    }

    @Override
    public List<Link> getAllBefore(OffsetDateTime time) {
        return jdbcTemplate.query(
            "select * from link where last_tracked < ?",
            rowMapper, time
        );
    }

    @Override
    public void updateLastTrackedTime(List<Long> linkIds, OffsetDateTime time) {
        jdbcTemplate.batchUpdate(
            "update link set last_tracked = ? where id = ?",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, time);
                    ps.setLong(2, linkIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return linkIds.size();
                }
            }
        );
    }
}
