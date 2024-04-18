package edu.java.scrapper.client.bot;

import edu.java.resilience.dto.LinkUpdateRequest;
import edu.java.resilience.dto.LinkUpdateResponse;
import edu.java.resilience.error.ClientRetryException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

@Retryable(retryFor = ClientRetryException.class,
           interceptor = "botInterceptor")
public interface BotClient {
    @PostExchange("/updates")
    LinkUpdateResponse sendUpdatesOnLink(@RequestBody LinkUpdateRequest body);
}
