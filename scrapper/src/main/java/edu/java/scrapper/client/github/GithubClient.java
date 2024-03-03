package edu.java.scrapper.client.github;

import edu.java.scrapper.client.github.dto.CommitsListItem;
import edu.java.scrapper.client.github.dto.Repository;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@Component
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
