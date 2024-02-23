package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.stackoverflow.dto.StackOverflowAnswersResponse;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest(httpPort = 8080)
class StackOverflowClientTest {
    public StackOverflowClient client = StackOverflowClientBuilder.build("http://localhost:8080");
    public String answers = """
        {
          "items": [
            {
              "owner": {
                "account_id": 559988,
                "reputation": 467517,
                "user_id": 922184,
                "user_type": "registered",
                "profile_image": "https://i.stack.imgur.com/h7WDB.jpg?s=256&g=1",
                "display_name": "Mysticial",
                "link": "https://stackoverflow.com/users/922184/mysticial"
              },
              "is_accepted": true,
              "score": 34929,
              "last_activity_date": 1706021923,
              "last_edit_date": 1706021923,
              "creation_date": 1340805402,
              "answer_id": 11227902,
              "question_id": 11227809,
              "content_license": "CC BY-SA 4.0"
            },
            {
              "owner": {
                "account_id": 461945,
                "reputation": 33022,
                "user_id": 863980,
                "user_type": "registered",
                "accept_rate": 83,
                "profile_image": "https://www.gravatar.com/avatar/ea08c9063ae1de86e8d576fb569d513a?s=256&d=identicon&r=PG",
                "display_name": "vulcan raven",
                "link": "https://stackoverflow.com/users/863980/vulcan-raven"
              },
              "is_accepted": false,
              "score": 2620,
              "last_activity_date": 1704220366,
              "last_edit_date": 1704220366,
              "creation_date": 1341282330,
              "answer_id": 11303693,
              "question_id": 11227809,
              "content_license": "CC BY-SA 4.0"
            }
          ],
          "has_more": true,
          "quota_max": 10000,
          "quota_remaining": 9988
        }
        """;

    @Test
    @DisplayName("Test if getAllByQuestion() satisfies api")
    void getAllByQuestion() {
        stubFor(get(urlPathMatching("/2\\.3/questions/123/answers"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(answers)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        StackOverflowAnswersResponse response = client.getAllByQuestion(123L);

        assertThat(response)
            .isNotNull();
        assertThat(response.answers())
            .asList()
            .isNotEmpty()
            .hasSize(2);
        assertThat(response.answers().get(0).lastActivity())
            .isEqualTo(Instant.ofEpochSecond(1706021923).atOffset(ZoneOffset.UTC));
    }

    @Test
    @DisplayName("Test if getAllByQuestionFromDate() satisfies api")
    void getAllByQuestionFromDate() {
        stubFor(get(urlPathMatching("/2\\.3/questions/123/answers"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .withQueryParam("fromdate", equalTo("1704220366"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(answers)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        StackOverflowAnswersResponse response =
            client.getAllByQuestionFromDate(123L, 1704220366L);

        assertThat(response)
            .isNotNull();
        assertThat(response.answers())
            .asList()
            .isNotEmpty()
            .hasSize(2);
        assertThat(response.answers().get(0).lastActivity())
            .isEqualTo(Instant.ofEpochSecond(1706021923).atOffset(ZoneOffset.UTC));
    }
}
