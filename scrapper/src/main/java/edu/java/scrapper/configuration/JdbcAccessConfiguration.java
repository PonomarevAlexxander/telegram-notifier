package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.Chat;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.repository.TrackRepository;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTrackRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@Import(ScrapperServiceConfiguration.class)
public class JdbcAccessConfiguration {
    @Bean
    @SuppressWarnings("MagicNumber")
    public RowMapper<Link> linkRowMapper() {
        return (rs, rowNum) -> new Link(
            rs.getLong(1),
            URI.create(rs.getString(2)),
            rs.getObject(3, OffsetDateTime.class)
        );
    }

    @Bean
    public RowMapper<Chat> chatRowMapper() {
        return (rs, rowNum) -> new Chat(rs.getLong(1));
    }

    @Bean
    LinkRepository linkRepository(JdbcTemplate template, RowMapper<Link> rowMapper) {
        return new JdbcLinkRepository(template, rowMapper);
    }

    @Bean
    ChatRepository chatRepository(JdbcTemplate template, RowMapper<Chat> rowMapper) {
        return new JdbcChatRepository(template, rowMapper);
    }

    @Bean
    TrackRepository trackRepository(JdbcTemplate template) {
        return new JdbcTrackRepository(template);
    }
}
