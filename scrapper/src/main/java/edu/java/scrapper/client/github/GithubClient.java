package edu.java.scrapper.client.github;

import edu.java.scrapper.client.github.dto.Repository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@Component
public interface GithubClient {
    @GetExchange("/repos/{user}/{repository}")
    Repository getRepository(@PathVariable String user, @PathVariable String repository);
}
