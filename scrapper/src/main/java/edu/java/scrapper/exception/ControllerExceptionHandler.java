package edu.java.scrapper.exception;

import com.giffing.bucket4j.spring.boot.starter.context.RateLimitException;
import edu.java.resilience.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse globalExceptionHandler(Exception ex, WebRequest request) {
        return ApiErrorResponse.fromException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        HttpMediaTypeNotSupportedException.class,
        MethodArgumentNotValidException.class,
        LinkIsNotSupportedException.class,
        HandlerMethodValidationException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse badRequestHandler(Exception ex, WebRequest request) {
        return ApiErrorResponse.fromException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        ResourceAlreadyExistException.class
    })
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiErrorResponse duplicateHandler(Exception ex, WebRequest request) {
        return ApiErrorResponse.fromException(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
        NoResourceFoundException.class,
        ResourceNotExistException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse notFoundHandler(Exception ex, WebRequest request) {
        return ApiErrorResponse.fromException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RateLimitException.class})
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    protected ApiErrorResponse ratelimitHandler(Exception ex, WebRequest request) {
        return ApiErrorResponse.fromException(ex, request, HttpStatus.TOO_MANY_REQUESTS);
    }
}
