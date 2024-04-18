package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.resilience.config.HttpCodeConverter;
import edu.java.resilience.error.ClientRetryException;
import edu.java.resilience.error.HttpClientErrorHandler;
import java.time.Duration;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static edu.java.bot.configuration.BotConfiguration.interceptor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@WireMockTest(httpPort = 8080)
@RestClientTest
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class ScrapperClientRetryTest {
    private static final int ATTEMPTS = 3;
    @TestConfiguration
    public static class Config {

        @Bean
        public RetryOperationsInterceptor scrapperInterceptor() {
            return interceptor(new ApplicationConfig.Retry(
                Set.of(HttpStatusCode.valueOf(500)),
                ApplicationConfig.BackoffStrategy.LINEAR,
                Duration.ofSeconds(1), ATTEMPTS
            ));
        }

        @Bean
        public ScrapperClient scrapperClient() {
            return ScrapperClientBuilder.build(
                RestClient.builder(),
                "http://localhost:8080",
                new HttpClientErrorHandler(Set.of(HttpStatusCode.valueOf(500)))
            );
        }

        @Bean
        @ConfigurationPropertiesBinding
        public HttpCodeConverter httpCodeConverter() {
            return new HttpCodeConverter();
        }
    }

    @Autowired
    public ScrapperClient client;

    @Test
    @DisplayName("Test if client retries failed responses")
    public void testRetryableOnClientException() {
        stubFor(post(urlPathMatching("/tg-chat/\\d+"))
            .willReturn(aResponse().withStatus(500)));

        assertThatThrownBy(() -> client.registerNewChat(123L)).isInstanceOf(ClientRetryException.class);

        verify(ATTEMPTS, postRequestedFor(urlPathMatching("/tg-chat/123")));
    }
}

