package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.github.dto.Repository;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest(httpPort = 8080)
class GithubClientTest {
    public GithubClient client = GithubClientBuilder.build("http://localhost:8080");

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
}
