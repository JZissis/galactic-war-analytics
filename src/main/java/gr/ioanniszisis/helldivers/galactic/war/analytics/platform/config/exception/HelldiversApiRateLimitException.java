package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.exception;

import lombok.Getter;

import module java.base;

/**
 * Thrown when the Helldivers API answers with {@code 429 Too Many Requests}.
 *
 * <p>Carries the wait time from the response's {@code Retry-After} header, so the retry policy
 * and the error response can both use it.
 */
@Getter
public class HelldiversApiRateLimitException extends RuntimeException {

    /** How long to wait before calling the API again. */
    private final Duration retryAfter;

    /**
     * Creates the exception.
     *
     * @param retryAfter how long to wait before calling the API again
     */
    public HelldiversApiRateLimitException(Duration retryAfter) {
        super("HellDivers API rate limit exceeded, retry after " + retryAfter);
        this.retryAfter = retryAfter;
    }
}
