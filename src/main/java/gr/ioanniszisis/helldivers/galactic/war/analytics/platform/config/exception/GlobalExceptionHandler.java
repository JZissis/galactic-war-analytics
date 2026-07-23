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

@SuppressWarnings("NullableProblems")
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ProblemDetail> handleException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("An unexpected error occurred");
        log.error("Unhandled exception", ex);
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HelldiversApiRateLimitException.class, RequestNotPermitted.class})
    public ResponseEntity<ProblemDetail> handleRateLimitException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problemDetail.setDetail("Rate limit exceeded");
        log.warn("Caught exception with info: {}", ex.getMessage());
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ResourceAccessException.class})
    public ResponseEntity<ProblemDetail> handleResourceAccessException(ResourceAccessException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        problemDetail.setDetail("The Helldivers API did not respond");
        log.warn("Helldivers API unreachable: {}", ex.getMessage());
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler({RestClientResponseException.class})
    public ResponseEntity<ProblemDetail> handleRestClientResponseException(RestClientResponseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setDetail("Bad gateway error. Status code: " + ex.getStatusCode());
        log.warn("Helldivers API returned error status: {}", ex.getStatusCode(), ex);
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler({HelldiversApiEmptyResponseException.class})
    public ResponseEntity<ProblemDetail> handleEmptyResponseException(HelldiversApiEmptyResponseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setDetail("The Helldivers API returned an empty response");
        log.warn("Helldivers API returned an empty response: {}", ex.getMessage());
        return buildProblemDetailResponseEntity(ex, problemDetail, HttpStatus.BAD_GATEWAY);
    }

    private static ResponseEntity<ProblemDetail> buildProblemDetailResponseEntity(Exception ex, ProblemDetail problemDetail, HttpStatus status) {
        if (ex instanceof HelldiversApiRateLimitException hex) {
            return ResponseEntity.status(status).header(HttpHeaders.RETRY_AFTER, String.valueOf(hex.getRetryAfter().toSeconds())).body(problemDetail);
        } else if (ex instanceof RequestNotPermitted) {
            String retryAfterSeconds = String.valueOf(HelldiversApiResilienceConfig.LIMIT_REFRESH_PERIOD.toSeconds());
            return ResponseEntity.status(status).header(HttpHeaders.RETRY_AFTER, retryAfterSeconds).body(problemDetail);
        }
        return ResponseEntity.status(status).body(problemDetail);
    }
}
