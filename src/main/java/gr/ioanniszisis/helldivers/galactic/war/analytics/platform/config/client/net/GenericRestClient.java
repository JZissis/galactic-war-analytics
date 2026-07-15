package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class GenericRestClient {

    private final RestClient restClient;

    // Add methods to interact with the REST API
    public <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, Class<T> responseType) {
        return this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.build(uriVariables))
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType);
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return this.restClient.get()
                .uri(url, UriBuilder::build)
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType);
    }

    public <T> ResponseEntity<T> get(String url, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.queryParams(queryParams).build())
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType);
    }

    public <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return this.restClient.get()
                .uri(url, (uriBuilder) -> uriBuilder.queryParams(queryParams).build(uriVariables))
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders()))
                .retrieve()
                .toEntity(responseType);
    }

    public <T> ResponseEntity<T> get(String url, HttpHeaders additionalHeaders, Class<T> responseType) {
        return this.restClient.get()
                .uri(url, UriBuilder::build)
                .headers((httpHeaders) -> httpHeaders.addAll(constructHeaders(additionalHeaders)))
                .retrieve()
                .toEntity(responseType);
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
