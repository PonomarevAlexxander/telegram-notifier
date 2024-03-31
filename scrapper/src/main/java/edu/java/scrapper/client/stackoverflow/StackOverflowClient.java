package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.stackoverflow.dto.StackOverflowAnswersResponse;
import edu.java.scrapper.exception.ClientRetryException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@SuppressWarnings("LineLength")
@Retryable(retryFor = ClientRetryException.class,
           interceptor = "stackOverflowInterceptor")
public interface StackOverflowClient {
    @GetExchange("/2.3/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow")
    StackOverflowAnswersResponse getAllByQuestion(@PathVariable Long id);

    @GetExchange(
        "/2.3/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow&fromdate={epochSecond}&filter=!6WPIomp1bT*YI"
    )
    StackOverflowAnswersResponse getAllByQuestionFromDate(@PathVariable Long id, @PathVariable Long epochSecond);
}
