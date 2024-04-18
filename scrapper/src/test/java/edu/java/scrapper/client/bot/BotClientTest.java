package edu.java.scrapper.client.bot;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.resilience.dto.LinkUpdateRequest;
import edu.java.resilience.dto.LinkUpdateResponse;
import edu.java.resilience.error.HttpClientErrorHandler;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest(httpPort = 8080)
class BotClientTest {
    public BotClient client = BotClientBuilder.build(
        RestClient.builder(),
        "http://localhost:8080",
        new HttpClientErrorHandler(new HashSet<>())
    );

    @Test
    @DisplayName("Test if getAllLinks() satisfies API on success")
    void getAllLinks_success() {
        stubFor(post(urlPathMatching("/updates"))
            .withRequestBody(equalToJson("""
                {
                    "id": "${json-unit.any-number}",
                    "url": "${json-unit.any-string}",
                    "description": "${json-unit.any-string}",
                    "tgChatIds": [123]
                }"""))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "description": "Good for you"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        LinkUpdateResponse someUpdate = client.sendUpdatesOnLink(new LinkUpdateRequest(
            0L,
            URI.create("https://github.com"),
            "some update",
            List.of(123L)
        ));

        assertThat(someUpdate.description())
            .isEqualTo("Good for you");
    }

    @Test
    @DisplayName("Test if getAllLinks() satisfies API on failure")
    void getAllLinks_failure() {
        stubFor(post(urlPathMatching("/updates"))
            .willReturn(aResponse()
                .withStatus(404)
                .withBody("""
                    {
                        "description": "Bad for you",
                        "code": "404 BAD REQUEST",
                        "exceptionName": "SomeException",
                        "exceptionMessage": "Blip blob your fault",
                        "stacktrace": []
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        assertThatThrownBy(() -> client.sendUpdatesOnLink(new LinkUpdateRequest(
            0L,
            URI.create("https://github.com"),
            "some update",
            List.of(123L)
        ))).isInstanceOf(RestClientResponseException.class);
    }
}
