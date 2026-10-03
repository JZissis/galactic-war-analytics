package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

/**
 * Thin wrapper around Spring's {@link RestClient} for JSON {@code GET} calls.
 *
 * <p>Every call sends default {@code Accept} and {@code Accept-Charset} headers and runs through
 * the optional {@link RateLimiter} and {@link Retry} decorators, so callers do not deal with
 * resilience concerns themselves.
 */
public class GenericRestClient {

    private final RestClient restClient;
    private final RateLimiter rateLimiter;
    private final Retry retry;

    /**
     * Creates a client without rate limiting or retries.
     *
     * @param restClient the underlying Spring client
     */
    public GenericRestClient(RestClient restClient) {
        this(restClient, null, null);
    }

    /**
     * Creates a client with resilience decorators.
     *
     * @param restClient  the underlying Spring client
     * @param rateLimiter rate limiter applied to every call, or {@code null} for none
     * @param retry       retry policy applied to every call, or {@code null} for none
     */
    public GenericRestClient(RestClient restClient, RateLimiter rateLimiter, Retry retry) {
        this.restClient = restClient;
        this.rateLimiter = rateLimiter;
        this.retry = retry;
    }

    /**
     * Sends a {@code GET} request with URI template variables.
     *
     * @param url          the URL or URL template, relative to the base URL
     * @param uriVariables values for the template variables in {@code url}
     * @param responseType the class to map the response body to
     * @param <T>          the response body type
     * @return the response, with the body mapped to {@code responseType}
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.build(uriVariables))
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    /**
     * Sends a {@code GET} request.
     *
     * @param url          the URL, relative to the base URL
     * @param responseType the class to map the response body to
     * @param <T>          the response body type
     * @return the response, with the body mapped to {@code responseType}
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, UriBuilder::build)
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    /**
     * Sends a {@code GET} request with query parameters.
     *
     * @param url          the URL, relative to the base URL
     * @param queryParams  the query parameters to append
     * @param responseType the class to map the response body to
     * @param <T>          the response body type
     * @return the response, with the body mapped to {@code responseType}
     */
    public <T> ResponseEntity<T> get(String url, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.queryParams(queryParams).build())
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    /**
     * Sends a {@code GET} request with URI template variables and query parameters.
     *
     * @param url          the URL or URL template, relative to the base URL
     * @param uriVariables values for the template variables in {@code url}
     * @param queryParams  the query parameters to append
     * @param responseType the class to map the response body to
     * @param <T>          the response body type
     * @return the response, with the body mapped to {@code responseType}
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.queryParams(queryParams).build(uriVariables))
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    /**
     * Sends a {@code GET} request with extra headers.
     *
     * @param url               the URL, relative to the base URL
     * @param additionalHeaders headers to send on top of the defaults; they replace defaults with the same name
     * @param responseType      the class to map the response body to
     * @param <T>               the response body type
     * @return the response, with the body mapped to {@code responseType}
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders additionalHeaders, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, UriBuilder::build)
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders(additionalHeaders)))
                .retrieve()
                .toEntity(responseType));
    }

    /**
     * Runs the call through the configured resilience decorators. The rate limiter is the
     * innermost decorator so every retry attempt acquires a fresh permit.
     *
     * @param apiCall the HTTP call to run
     * @param <T>     the result type
     * @return the result of the first successful attempt
     */
    private <T> T execute(Supplier<T> apiCall) {
        Supplier<T> decorated = apiCall;
        if (nonNull(rateLimiter)) {
            decorated = RateLimiter.decorateSupplier(rateLimiter, decorated);
        }
        if (nonNull(retry)) {
            decorated = Retry.decorateSupplier(retry, decorated);
        }
        return decorated.get();
    }

    private static HttpHeaders constructHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(defaultHeaders());
        return headers;
    }

    private static HttpHeaders constructHeaders(HttpHeaders additionalHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(defaultHeaders());
        addCustomHeaders(headers, additionalHeaders);
        return headers;
    }

    private static void addCustomHeaders(HttpHeaders headers, HttpHeaders customHeaders) {
        if (nonNull(customHeaders)) {
            headers.putAll(customHeaders);
        }
    }

    private static Map<String, List<String>> defaultHeaders() {
        return Map.of("Accept", Collections.singletonList("application/json"),
                "Accept-Charset", Collections.singletonList(StandardCharsets.UTF_8.name()));
    }
}
