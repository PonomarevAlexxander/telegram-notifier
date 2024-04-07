package edu.java.scrapper.configuration.db;

import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.repository.TrackRepository;
import edu.java.scrapper.repository.jooq.JooqChatRepository;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.repository.jooq.JooqTrackRepository;
import org.jooq.DSLContext;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@Import(ScrapperServiceConfiguration.class)
public class JooqAccessConfiguration {
    @Bean
    LinkRepository linkRepository(DSLContext context) {
        return new JooqLinkRepository(context);
    }

    @Bean
    ChatRepository chatRepository(DSLContext context) {
        return new JooqChatRepository(context);
    }

    @Bean
    TrackRepository trackRepository(DSLContext context) {
        return new JooqTrackRepository(context);
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
