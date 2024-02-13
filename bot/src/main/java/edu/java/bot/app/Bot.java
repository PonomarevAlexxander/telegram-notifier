package edu.java.bot.app;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

public interface Bot extends AutoCloseable, UpdatesListener {
    void start();

    <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request);
}
