package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.resilience.config.HttpCodeConverter;
import edu.java.resilience.error.ClientRetryException;
import edu.java.resilience.error.HttpClientErrorHandler;
import edu.java.scrapper.configuration.ApplicationConfig;
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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static edu.java.scrapper.configuration.client.ClientConfiguration.interceptor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@WireMockTest(httpPort = 8080)
@RestClientTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class GithubClientRetryTest {
    private static final int ATTEMPTS = 3;
    @TestConfiguration
    public static class Config {
        @Bean
        public RetryOperationsInterceptor githubInterceptor() {
            return interceptor(new ApplicationConfig.Retry(
                Set.of(HttpStatusCode.valueOf(500)),
                ApplicationConfig.BackoffStrategy.LINEAR,
                Duration.ofSeconds(1), ATTEMPTS
            ));
        }

        @Bean
        public GithubClient githubClient() {
            return GithubClientBuilder.build(
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
    public GithubClient client;

    @Test
    @DisplayName("Test if client retries failed responses")
    public void testRetryableOnClientException() {
        stubFor(get("/repos/somename/repo")
            .willReturn(aResponse().withStatus(500)));

        assertThatThrownBy(() -> client.getRepository("somename", "repo")).isInstanceOf(ClientRetryException.class);

        verify(ATTEMPTS, getRequestedFor(urlEqualTo("/repos/somename/repo")));
    }
}

