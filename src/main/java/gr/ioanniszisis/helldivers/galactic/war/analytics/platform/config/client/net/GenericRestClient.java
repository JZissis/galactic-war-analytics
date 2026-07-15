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

public class GenericRestClient {

    private final RestClient restClient;
    private final RateLimiter rateLimiter;
    private final Retry retry;

    public GenericRestClient(RestClient restClient) {
        this(restClient, null, null);
    }

    public GenericRestClient(RestClient restClient, RateLimiter rateLimiter, Retry retry) {
        this.restClient = restClient;
        this.rateLimiter = rateLimiter;
        this.retry = retry;
    }

    public <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.build(uriVariables))
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, UriBuilder::build)
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    public <T> ResponseEntity<T> get(String url, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.queryParams(queryParams).build())
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

    public <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return execute(() -> this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.queryParams(queryParams).build(uriVariables))
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType));
    }

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
