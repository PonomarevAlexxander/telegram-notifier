package edu.java.bot.exception;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotifierExceptionHandler implements ExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(NotifierExceptionHandler.class);

    @Override
    public void onException(TelegramException e) {
        if (e.response() != null) {
            LOGGER.error(
                "telegram error code: {}, description: {}",
                e.response().errorCode(),
                e.response().description()
            );
        } else {
            LOGGER.error("some network error happened: {}", e.toString());
        }
    }
}
