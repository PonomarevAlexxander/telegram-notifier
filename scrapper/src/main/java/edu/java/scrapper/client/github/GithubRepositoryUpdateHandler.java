package edu.java.scrapper.client.github;

import edu.java.scrapper.client.UpdateHandler;
import edu.java.scrapper.client.github.dto.Repository;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GithubRepositoryUpdateHandler implements UpdateHandler {
    private final Pattern pattern = Pattern.compile("github\\.com/(?<user>[a-zA-Z-]*)/(?<repository>[a-zA-Z-_.0-9]*)$");
    private final GithubClient client;

    @Override
    public LinkUpdate getUpdate(Link link) {
        String uri = link.getUri().toString();
        String user = getUser(uri);
        String repo = getRepository(uri);

        Repository repository = client.getRepository(user, repo);
        OffsetDateTime lastUpdated = link.getLastTracked();
        if (repository.lastUpdated().isAfter(lastUpdated)) {
            List<String> commits =
                client.getRepositoryCommitsSince(user, repo, DateTimeFormatter.ISO_INSTANT.format(lastUpdated)).stream()
                    .map(commitsListItem -> commitsListItem.commit().message())
                    .toList();

            return new LinkUpdate(link, true, String.format("repository has some new commits: %s", commits));
        }
        return new LinkUpdate(link, false, "");
    }

    @Override
    public boolean supports(String link) {
        return pattern.matcher(link).find();
    }

    private String getUser(String uri) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group("user");
        }
        throw new IllegalArgumentException("no user name found in uri");
    }

    private String getRepository(String uri) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group("repository");
        }
        throw new IllegalArgumentException("no repository found in uri");
    }
}
