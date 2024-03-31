package edu.java.bot.client;

import edu.java.bot.client.dto.ChatResponse;
import edu.java.bot.client.dto.LinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.LinksResponse;
import edu.java.bot.exception.ClientRetryException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

@Retryable(retryFor = ClientRetryException.class,
           interceptor = "interceptor")
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
