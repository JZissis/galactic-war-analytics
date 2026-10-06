package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.exception.HelldiversApiRateLimitException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import module java.base;

/**
 * Resilience4j setup for calls to the Helldivers API.
 *
 * <p>Defines a client-side {@link RateLimiter} that stays under the API's documented limit, and a
 * {@link Retry} that retries rate-limit rejections and transient upstream failures. Both beans are applied
 * by {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient}.
 */
@Configuration
public class HelldiversApiResilienceConfig {

    /** 4 req / 10s keeps one request of headroom under the documented 5 req / 10s API limit. */
    private static final int LIMIT_FOR_PERIOD = 4;
    /** Public so the exception handler can advertise it as the {@code Retry-After} for local rate-limit rejections. */
    public static final Duration LIMIT_REFRESH_PERIOD = Duration.ofSeconds(10);
    /** Callers block for a permit up to one full refresh window instead of failing fast. */
    private static final Duration PERMIT_WAIT_TIMEOUT = Duration.ofSeconds(11);

    /**
     * With a 10s read timeout, 3 attempts and 1s + 2s of backoff, a call against a hung API gives up
     * after roughly 33s. A web request then gets a 504 instead of waiting indefinitely.
     */
    private static final int MAX_RETRY_ATTEMPTS = 3;
    /** Backoff before the 2nd attempt; doubles for each attempt after that (1s, 2s, ...). */
    private static final long INITIAL_BACKOFF_MILLIS = 1_000L;
    private static final double BACKOFF_MULTIPLIER = 2.0;
    private static final IntervalFunction BACKOFF =
            IntervalFunction.ofExponentialBackoff(INITIAL_BACKOFF_MILLIS, BACKOFF_MULTIPLIER);

    /**
     * Upstream statuses that mean "the API or its gateway is struggling right now". Other 5xx codes
     * (such as 500) usually mean a bug on their side that a retry will not fix.
     */
    private static final Set<Integer> RETRYABLE_STATUSES = Set.of(502, 503, 504);

    /**
     * Client-side rate limiter for the Helldivers API.
     *
     * @return a rate limiter that allows {@code LIMIT_FOR_PERIOD} requests per {@link #LIMIT_REFRESH_PERIOD}
     */
    @Bean
    public RateLimiter helldiversApiRateLimiter() {
        return RateLimiter.of("helldiversApi", RateLimiterConfig.custom()
                .limitForPeriod(LIMIT_FOR_PERIOD)
                .limitRefreshPeriod(LIMIT_REFRESH_PERIOD)
                .timeoutDuration(PERMIT_WAIT_TIMEOUT)
                .build());
    }

    /**
     * Retries failures that are likely to pass on their own: 429 rate-limit rejections, I/O failures
     * (timeouts, refused connections) and 502/503/504 responses. Waits use exponential backoff,
     * except after a 429, where the API's {@code Retry-After} wait is used instead.
     *
     * @return a retry policy with up to {@code MAX_RETRY_ATTEMPTS} attempts per call
     */
    @Bean
    public Retry helldiversApiRetry() {
        return Retry.of("helldiversApi", RetryConfig.custom()
                .maxAttempts(MAX_RETRY_ATTEMPTS)
                .retryOnException(HelldiversApiResilienceConfig::isRetryable)
                .intervalBiFunction((attempt, either) -> {
                    if (either.isLeft()
                            && either.getLeft() instanceof HelldiversApiRateLimitException rateLimitException
                            && rateLimitException.getRetryAfter() != null) {
                        return rateLimitException.getRetryAfter().toMillis();
                    }
                    return BACKOFF.apply(attempt);
                })
                .build());
    }

    private static boolean isRetryable(Throwable throwable) {
        return throwable instanceof HelldiversApiRateLimitException
                || throwable instanceof ResourceAccessException
                || (throwable instanceof HttpServerErrorException serverError
                    && RETRYABLE_STATUSES.contains(serverError.getStatusCode().value()));
    }
}
