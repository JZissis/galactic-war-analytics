package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.HttpClientFactory;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.RateLimitInterceptor;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.exception.HelldiversApiRateLimitException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Builds the {@link GenericRestClient} used to call the Helldivers API.
 *
 * <p>The client uses a pooled Apache HttpClient, sends the {@code X-Super-Client} and
 * {@code X-Super-Contact} headers the API asks for, logs rate-limit headers, and turns
 * {@code 429 Too Many Requests} responses into {@link HelldiversApiRateLimitException}.
 */
@Configuration
public class HellDiversApiRestClientConfig {

    /** Fallback when a 429 arrives without a Retry-After header: wait out a full rate-limit window. */
    private static final Duration DEFAULT_RETRY_AFTER = Duration.ofSeconds(10);

    /**
     * Creates the Helldivers API client.
     *
     * @param hellDiversApiRestClientProperties base URL, identification headers and HTTP client settings
     * @param meterRegistry                     registry for connection pool metrics
     * @param helldiversApiRateLimiter          rate limiter applied to every call
     * @param helldiversApiRetry                retry policy applied to every call
     * @return the configured client
     */
    @Bean
    public GenericRestClient helldiversApiRestClient(HellDiversApiRestClientProperties hellDiversApiRestClientProperties,
                                                     MeterRegistry meterRegistry,
                                                     RateLimiter helldiversApiRateLimiter,
                                                     Retry helldiversApiRetry) {

        RestClient restClient = RestClient.builder()
                .requestFactory(HttpClientFactory.createHttpClientFactory(hellDiversApiRestClientProperties, meterRegistry))
                .baseUrl(hellDiversApiRestClientProperties.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.add("X-Super-Client", hellDiversApiRestClientProperties.getSuperClient());
                    headers.add("X-Super-Contact", hellDiversApiRestClientProperties.getSuperContact());
                })
                .requestInterceptor(new RateLimitInterceptor())
                .defaultStatusHandler(
                        status -> status.value() == HttpStatus.TOO_MANY_REQUESTS.value(),
                        (request, response) -> {
                            throw new HelldiversApiRateLimitException(parseRetryAfter(response.getHeaders()));
                        })
                .build();

        return new GenericRestClient(restClient, helldiversApiRateLimiter, helldiversApiRetry);
    }

    /**
     * Reads the {@code Retry-After} header of a 429 response.
     *
     * @param headers the response headers
     * @return the wait time from the header in seconds, or {@code DEFAULT_RETRY_AFTER} when the
     *         header is missing or is not a number
     */
    private static Duration parseRetryAfter(HttpHeaders headers) {
        String retryAfter = headers.getFirst(HttpHeaders.RETRY_AFTER);
        if (retryAfter != null) {
            try {
                return Duration.ofSeconds(Long.parseLong(retryAfter.trim()));
            } catch (NumberFormatException ignored) {
                // Retry-After may also be an HTTP-date; fall through to the default window
            }
        }
        return DEFAULT_RETRY_AFTER;
    }
}
