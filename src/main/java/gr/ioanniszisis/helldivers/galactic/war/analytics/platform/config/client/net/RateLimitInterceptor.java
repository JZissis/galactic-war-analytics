package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import module java.base;

/**
 * Logs the Helldivers API rate-limit budget after each response.
 *
 * <p>Reads the {@code X-RateLimit-Remaining} header: logs it at DEBUG, and at WARN when one or zero
 * requests remain in the current window. The interceptor only observes; it never blocks or retries.
 */
@Slf4j
public class RateLimitInterceptor implements ClientHttpRequestInterceptor {

    private static final String RATE_LIMIT_REMAINING_HEADER = "X-RateLimit-Remaining";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ClientHttpResponse response = execution.execute(request, body);

        String remaining = response.getHeaders().getFirst(RATE_LIMIT_REMAINING_HEADER);
        if (remaining != null) {
            log.debug("HellDivers API rate limit remaining: {} (request: {})", remaining, request.getURI());
            if ("0".equals(remaining.trim()) || "1".equals(remaining.trim())) {
                log.warn("HellDivers API rate limit nearly exhausted: {} request(s) remaining in current window", remaining);
            }
        }
        return response;
    }
}
