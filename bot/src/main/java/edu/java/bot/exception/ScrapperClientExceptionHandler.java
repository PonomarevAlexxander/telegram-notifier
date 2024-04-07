package edu.java.bot.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.resilience.dto.ApiErrorResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ScrapperClientExceptionHandler implements ClientExceptionHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public SendMessage onClientException(Update update, RestClientResponseException exception) {
        ApiErrorResponse errorResponse = null;
        try {
            errorResponse = mapper.readValue(exception.getResponseBodyAsByteArray(), ApiErrorResponse.class);
        } catch (IOException e) {
            return new SendMessage(
                update.message().chat().id(),
                "Request failed with some internal error"
            );
        }
        return new SendMessage(
            update.message().chat().id(),
            String.format("Sorry, but %s.", errorResponse.exceptionMessage())
        );
    }
}
