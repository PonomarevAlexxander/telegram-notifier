package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.dto.ChatResponse;
import edu.java.bot.client.dto.LinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.LinksResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest(httpPort = 8080)
class ScrapperClientTest {
    public ScrapperClient client = ScrapperClientBuilder.build(WebClient.builder(), "http://localhost:8080");

    @Test
    @DisplayName("Test if registerNewChat() satisfies API on success")
    void registerNewChat() {
        stubFor(post(urlPathMatching("/tg-chat/\\d+"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "description": "Good for you"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        ChatResponse chatResponse = client.registerNewChat(123L);

        assertThat(chatResponse.description())
            .isEqualTo("Good for you");
    }

    @Test
    @DisplayName("Test if deleteChat() satisfies API on success")
    void deleteChat() {
        stubFor(delete(urlPathMatching("/tg-chat/\\d+"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "description": "Good for you"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        ChatResponse chatResponse = client.deleteChat(123L);

        assertThat(chatResponse.description())
            .isEqualTo("Good for you");
    }

    @Test
    @DisplayName("Test if getAllTrackedLinks() satisfies API on success")
    void getAllTrackedLinks() {
        stubFor(get(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", matching("\\d+"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "links": [
                            {
                                "id": 123,
                                "url": "https://some.com"
                            }
                        ],
                        "size": 1
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        LinksResponse allTrackedLinks = client.getAllTrackedLinks(123L);

        assertThat(allTrackedLinks.links())
            .asList()
            .isNotEmpty();
        assertThat(allTrackedLinks.size())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Test if trackNew() satisfies API on success")
    void trackNew() {
        stubFor(post(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", matching("\\d+"))
            .withRequestBody(equalToJson("""
                {
                    "url": "${json-unit.any-string}"
                }"""))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "id": 123,
                        "url": "https://some.com"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        LinkResponse linkResponse = client.trackNew(123L, new LinkRequest("https://some.com"));

        assertThat(linkResponse.url().toString())
            .isEqualTo("https://some.com");
    }

    @Test
    @DisplayName("Test if untrackLink() satisfies API on success")
    void untrackLink() {
        stubFor(delete(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", matching("\\d+"))
            .withRequestBody(equalToJson("""
                {
                    "url": "${json-unit.any-string}"
                }"""))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("""
                    {
                        "id": 123,
                        "url": "https://some.com"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        LinkResponse linkResponse = client.untrackLink(123L, new LinkRequest("https://some.com"));

        assertThat(linkResponse.url().toString())
            .isEqualTo("https://some.com");
    }
}
