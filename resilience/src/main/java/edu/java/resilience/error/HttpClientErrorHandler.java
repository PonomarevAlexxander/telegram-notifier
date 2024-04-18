package edu.java.resilience.error;

import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Component
@RequiredArgsConstructor
public class HttpClientErrorHandler extends DefaultResponseErrorHandler {
    private final Set<HttpStatusCode> retryStatusCodes;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return super.hasError(response) || retryStatusCodes.contains(response.getStatusCode());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (retryStatusCodes.contains(response.getStatusCode())) {
            throw new ClientRetryException("Need to retry request");
        }
        super.handleError(response);
    }
}
