package edu.java.bot.exception;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.web.client.RestClientResponseException;

public interface ClientExceptionHandler {
    SendMessage onClientException(Update update, RestClientResponseException exception);
}
