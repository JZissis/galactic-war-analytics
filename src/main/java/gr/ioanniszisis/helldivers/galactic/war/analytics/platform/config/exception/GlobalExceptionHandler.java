package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.exception;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.HelldiversApiResilienceConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Maps exceptions thrown while serving web requests to RFC 9457 {@link ProblemDetail} responses.
 *
 * <ul>
 *   <li>Rate limit hit (ours or the Helldivers API's): {@code 503} with a {@code Retry-After} header</li>
 *   <li>Helldivers API unreachable or timed out: {@code 504}</li>
 *   <li>Helldivers API error status or empty body: {@code 502}</li>
 *   <li>Anything else: {@code 500} with a generic message; the stack trace is logged</li>
 * </ul>
 *
 * <p>Only covers Spring MVC requests. Scheduled jobs must handle their own exceptions.
 */
@SuppressWarnings("NullableProblems")
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Fallback for any exception without a more specific handler.
     *
     * @param ex the unhandled exception
     * @return a {@code 500 Internal Server Error} problem detail
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ProblemDetail> handleException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("An unexpected error occurred");
        log.error("Unhandled exception", ex);
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles a rate limit hit, either from the Helldivers API (429) or from the local rate limiter.
     *
     * @param ex the rate-limit exception
     * @return a {@code 503 Service Unavailable} problem detail
     */
    @ExceptionHandler({HelldiversApiRateLimitException.class, RequestNotPermitted.class})
    public ResponseEntity<ProblemDetail> handleRateLimitException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problemDetail.setDetail("Rate limit exceeded");
        log.warn("Caught exception with info: {}", ex.getMessage());
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles I/O failures, such as timeouts or refused connections, when calling the Helldivers API.
     *
     * @param ex the I/O exception
     * @return a {@code 504 Gateway Timeout} problem detail
     */
    @ExceptionHandler({ResourceAccessException.class})
    public ResponseEntity<ProblemDetail> handleResourceAccessException(ResourceAccessException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        problemDetail.setDetail("The Helldivers API did not respond");
        log.warn("Helldivers API unreachable: {}", ex.getMessage());
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.GATEWAY_TIMEOUT);
    }

    /**
     * Handles error status codes returned by the Helldivers API.
     *
     * @param ex the exception carrying the upstream status
     * @return a {@code 502 Bad Gateway} problem detail
     */
    @ExceptionHandler({RestClientResponseException.class})
    public ResponseEntity<ProblemDetail> handleRestClientResponseException(RestClientResponseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setDetail("Bad gateway error. Status code: " + ex.getStatusCode());
        log.warn("Helldivers API returned error status: {}", ex.getStatusCode(), ex);
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.BAD_GATEWAY);
    }

    /**
     * Handles a successful Helldivers API response that has no body.
     *
     * @param ex the empty-response exception
     * @return a {@code 502 Bad Gateway} problem detail
     */
    @ExceptionHandler({HelldiversApiEmptyResponseException.class})
    public ResponseEntity<ProblemDetail> handleEmptyResponseException(HelldiversApiEmptyResponseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setDetail("The Helldivers API returned an empty response");
        log.warn("Helldivers API returned an empty response: {}", ex.getMessage());
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.BAD_GATEWAY);
    }

    /**
     * Wraps a problem detail in a response, adding {@code Retry-After} for rate-limit exceptions.
     *
     * @param ex            the handled exception
     * @param problemDetail the response body
     * @param status        the response status
     * @return the response entity
     */
    private static ResponseEntity<ProblemDetail> buildProblemDetailResponseEntity(Exception ex, ProblemDetail problemDetail, HttpStatus status) {
        return switch (ex) {
            case HelldiversApiRateLimitException hex -> ResponseEntity.status(status)
                    .header(HttpHeaders.RETRY_AFTER, String.valueOf(hex.getRetryAfter().toSeconds()))
                    .body(problemDetail);
            case RequestNotPermitted _ -> ResponseEntity.status(status)
                    .header(HttpHeaders.RETRY_AFTER, String.valueOf(HelldiversApiResilienceConfig.LIMIT_REFRESH_PERIOD.toSeconds()))
                    .body(problemDetail);
            default -> ResponseEntity.status(status).body(problemDetail);
        };
    }
}
