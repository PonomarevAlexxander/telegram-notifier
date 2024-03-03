package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.UpdateHandler;
import edu.java.scrapper.client.stackoverflow.dto.StackOverflowAnswersResponse;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.time.OffsetDateTime;
import java.util.List;
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
        String id = getQuestionId(link.getUri().toString());
        OffsetDateTime lastUpdated = link.getLastTracked();
        StackOverflowAnswersResponse allByQuestion =
            client.getAllByQuestionFromDate(Long.parseLong(id), lastUpdated.toEpochSecond());
        if (!allByQuestion.answers().isEmpty()) {
            List<String> answers = allByQuestion.answers().stream()
                .map(answer -> String.format("Answer #%s \"%s\".", answer.answerId(), answer.message()))
                .toList();

            return new LinkUpdate(link, true, String.format("Some answers have been modified: %s", answers));
        }
        return new LinkUpdate(link, false, "");
    }

    @Override
    public boolean supports(String link) {
        return pattern.matcher(link).find();
    }

    private String getQuestionId(String uri) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return matcher.group("id");
        }
        throw new IllegalArgumentException("no question id found in uri");
    }
}
