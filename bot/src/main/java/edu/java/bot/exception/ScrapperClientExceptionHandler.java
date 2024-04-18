package edu.java.bot.exception;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.resilience.dto.ApiErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ScrapperClientExceptionHandler implements ClientExceptionHandler {
    @Override
    public SendMessage onClientException(Update update, RestClientResponseException exception) {
        ApiErrorResponse errorResponse = exception.getResponseBodyAs(ApiErrorResponse.class);
        return new SendMessage(
            update.message().chat().id(),
            String.format("Operation failed with error: %s", errorResponse.exceptionMessage())
        );
    }
}
