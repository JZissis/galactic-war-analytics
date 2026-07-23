package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.exception.HelldiversApiRateLimitException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;

import java.time.Duration;

@Configuration
public class HelldiversApiResilienceConfig {

    /** 4 req / 10s keeps one request of headroom under the documented 5 req / 10s API limit. */
    private static final int LIMIT_FOR_PERIOD = 4;
    /** Public so the exception handler can advertise it as the {@code Retry-After} for local rate-limit rejections. */
    public static final Duration LIMIT_REFRESH_PERIOD = Duration.ofSeconds(10);
    /** Callers block for a permit up to one full refresh window instead of failing fast. */
    private static final Duration PERMIT_WAIT_TIMEOUT = Duration.ofSeconds(11);

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long DEFAULT_RETRY_INTERVAL_MILLIS = 500L;

    @Bean
    public RateLimiter helldiversApiRateLimiter() {
        return RateLimiter.of("helldiversApi", RateLimiterConfig.custom()
                .limitForPeriod(LIMIT_FOR_PERIOD)
                .limitRefreshPeriod(LIMIT_REFRESH_PERIOD)
                .timeoutDuration(PERMIT_WAIT_TIMEOUT)
                .build());
    }

    /**
     * Retries rate-limit rejections and transient I/O failures; a 429's Retry-After
     * duration takes precedence over the fixed backoff interval.
     */
    @Bean
    public Retry helldiversApiRetry() {
        return Retry.of("helldiversApi", RetryConfig.custom()
                .maxAttempts(MAX_RETRY_ATTEMPTS)
                .retryExceptions(HelldiversApiRateLimitException.class, ResourceAccessException.class)
                .intervalBiFunction((attempt, either) -> {
                    if (either.isLeft()
                            && either.getLeft() instanceof HelldiversApiRateLimitException rateLimitException
                            && rateLimitException.getRetryAfter() != null) {
                        return rateLimitException.getRetryAfter().toMillis();
                    }
                    return DEFAULT_RETRY_INTERVAL_MILLIS;
                })
                .build());
    }
}
