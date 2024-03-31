package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.bot.dto.LinkUpdateRequest;
import edu.java.scrapper.client.bot.dto.LinkUpdateResponse;
import edu.java.scrapper.exception.ClientRetryException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

@Retryable(retryFor = ClientRetryException.class,
           interceptor = "botInterceptor")
public interface BotClient {
    @PostExchange("/updates")
    LinkUpdateResponse sendUpdatesOnLink(@RequestBody LinkUpdateRequest body);
}
