package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.github.dto.CommitsListItem;
import edu.java.scrapper.client.github.dto.Repository;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest(httpPort = 8080)
class GithubClientTest {
    public GithubClient client = GithubClientBuilder.build(WebClient.builder(), "http://localhost:8080");

    @Test
    @DisplayName("Test if getRepository() satisfies api")
    void getRepository() {
        stubFor(get(urlPathMatching("/repos/somename/repo"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "updated_at": "2011-01-26T19:14:43Z"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        Repository repository = client.getRepository("somename", "repo");

        assertThat(repository)
            .isNotNull();
        assertThat(repository.lastUpdated())
            .isEqualTo(OffsetDateTime.parse("2011-01-26T19:14:43Z"));
    }

    @Test
    @DisplayName("Test if getRepositoryCommitsSince() satisfies api")
    void getRepositoryCommitsSince() {
        stubFor(get(urlPathMatching("/repos/somename/repo/commits"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    [
                      {
                        "commit": {
                          "message": "Add controllers layer for Bot and Scrapper\\n\\nAdds needed endpoints for Bot and Scrapper\\nusing RestController. Also adds support\\nfor swagger-ui API documentation.\\nImplements HTTP clients for interconnection\\ncommunication between services.\\nAdds some stubs for service and repository\\nlayer."
                        }
                      }
                    ]
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        List<CommitsListItem> repositoryCommits =
            client.getRepositoryCommitsSince(
                "somename",
                "repo",
                DateTimeFormatter.ISO_INSTANT.format(OffsetDateTime.MIN)
            );

        assertThat(repositoryCommits)
            .asList()
            .hasSize(1);
    }
}
