package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.exception;

import lombok.Getter;

import java.time.Duration;

@Getter
public class HelldiversApiRateLimitException extends RuntimeException {

    private final Duration retryAfter;

    public HelldiversApiRateLimitException(Duration retryAfter) {
        super("HellDivers API rate limit exceeded, retry after " + retryAfter);
        this.retryAfter = retryAfter;
    }
}
