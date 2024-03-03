package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.bot.dto.LinkUpdateRequest;
import edu.java.scrapper.client.bot.dto.LinkUpdateResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

@Component
public interface BotClient {
    @PostExchange("/updates")
    LinkUpdateResponse sendUpdatesOnLink(@RequestBody LinkUpdateRequest body);
}
