package edu.java.bot.client;

import edu.java.resilience.dto.ChatResponse;
import edu.java.resilience.dto.LinkRequest;
import edu.java.resilience.dto.LinkResponse;
import edu.java.resilience.dto.LinksResponse;
import edu.java.resilience.error.ClientRetryException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

@Retryable(retryFor = ClientRetryException.class,
           interceptor = "scrapperInterceptor")
public interface ScrapperClient {
    @PostExchange("/tg-chat/{id}")
    ChatResponse registerNewChat(@PathVariable Long id);

    @DeleteExchange("/tg-chat/{id}")
    ChatResponse deleteChat(@PathVariable Long id);

    @GetExchange("/links")
    LinksResponse getAllTrackedLinks(@RequestHeader("Tg-Chat-Id") Long chatId);

    @PostExchange("/links")
    LinkResponse trackNew(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody LinkRequest link);

    @DeleteExchange("/links")
    LinkResponse untrackLink(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody LinkRequest link);
}
