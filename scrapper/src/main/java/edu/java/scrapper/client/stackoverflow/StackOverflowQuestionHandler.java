package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.UpdateHandler;
import edu.java.scrapper.client.stackoverflow.dto.StackOverflowAnswersResponse;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StackOverflowQuestionHandler implements UpdateHandler {
    private final Pattern pattern = Pattern.compile("stackoverflow\\.com/questions/(?<id>\\d+)/[a-z-]+$");
    private final StackOverflowClient client;

    @Override
    public LinkUpdate getUpdate(Link link) {
        String id = getQuestionId(link.resource().toString());
        OffsetDateTime lastUpdated = link.lastTracked();
        StackOverflowAnswersResponse allByQuestion =
            client.getAllByQuestionFromDate(Long.parseLong(id), lastUpdated.toEpochSecond());
        if (!allByQuestion.answers().isEmpty()) {
            return new LinkUpdate(link, true, "some answers have been modified");
        }
        return new LinkUpdate(link, false, "");
    }

    @Override
    public boolean supports(String uri) {
        return pattern.matcher(uri).find();
    }

    private String getQuestionId(String uri) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group("id");
        }
        throw new IllegalArgumentException("no question id found in uri");
    }
}
