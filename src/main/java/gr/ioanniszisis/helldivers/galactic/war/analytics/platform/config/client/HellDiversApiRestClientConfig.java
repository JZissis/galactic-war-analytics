package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.HttpClientFactory;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.RateLimitInterceptor;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.exception.HelldiversApiRateLimitException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class HellDiversApiRestClientConfig {

    /** Fallback when a 429 arrives without a Retry-After header: wait out a full rate-limit window. */
    private static final Duration DEFAULT_RETRY_AFTER = Duration.ofSeconds(10);

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
