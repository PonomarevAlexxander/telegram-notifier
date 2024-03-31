package edu.java.scrapper.client.github;

import edu.java.scrapper.client.github.dto.CommitsListItem;
import edu.java.scrapper.client.github.dto.Repository;
import edu.java.scrapper.exception.ClientRetryException;
import java.util.List;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@Retryable(retryFor = ClientRetryException.class,
           interceptor = "githubInterceptor")
public interface GithubClient {
    @GetExchange("/repos/{user}/{repository}")
    Repository getRepository(@PathVariable String user, @PathVariable String repository);

    @GetExchange("/repos/{user}/{repository}/commits?since={date}")
    List<CommitsListItem> getRepositoryCommitsSince(
        @PathVariable String user,
        @PathVariable String repository,
        @PathVariable String date
    );
}
