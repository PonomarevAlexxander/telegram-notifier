package edu.java.bot.exception.dto;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) {
    public static ApiErrorResponse fromException(Exception ex, WebRequest request, HttpStatus status) {
        return new ApiErrorResponse(
            request.getDescription(false),
            status.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
