package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.net.HttpClientProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.hc5.PoolingHttpClientConnectionManagerMetricsBinder;
import lombok.experimental.UtilityClass;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import module java.base;

/**
 * Creates Apache HttpClient 5 based request factories for Spring's {@code RestClient}.
 *
 * <p>The factory uses a pooled connection manager sized and timed from {@link HttpClientProperties},
 * and can register pool metrics with Micrometer.
 */
@UtilityClass
public class HttpClientFactory {

    /**
     * Creates a custom HTTP client factory.
     *
     * @param httpClientProperties the properties for the HTTP client
     * @return the configured HttpComponentsClientHttpRequestFactory
     */
    public HttpComponentsClientHttpRequestFactory createHttpClientFactory(HttpClientProperties httpClientProperties) {

        return createHttpClientFactory(httpClientProperties, null);
    }

    /**
     * Creates a custom HTTP client factory with metrics.
     *
     * @param httpClientProperties the properties for the HTTP client
     * @param meterRegistry the MeterRegistry for metrics binding, or {@code null} to skip metrics
     * @return the configured HttpComponentsClientHttpRequestFactory
     */
    public HttpComponentsClientHttpRequestFactory createHttpClientFactory(HttpClientProperties httpClientProperties,
                                                                          MeterRegistry meterRegistry) {

        HttpClient httpClient = HttpClientFactory.createHttpClient(httpClientProperties, meterRegistry);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofMillis(httpClientProperties.getReadTimeoutMillis()));
        factory.setConnectionRequestTimeout(Duration.ofMillis(httpClientProperties.getRequestTimeoutMillis()));

        return factory;
    }

    private HttpClient createHttpClient(HttpClientProperties httpClientProperties,
                                        MeterRegistry meterRegistry) {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

        if (meterRegistry != null && httpClientProperties.getConnectionManagerMetricsTag() != null) {
            new PoolingHttpClientConnectionManagerMetricsBinder(connManager, httpClientProperties.getConnectionManagerMetricsTag()).bindTo(meterRegistry);
        }

        configureConnectionProperties(httpClientProperties, connManager);
        return httpClientBuilder.setConnectionManager(connManager).build();
    }

    private void configureConnectionProperties(HttpClientProperties httpClientProperties,
                                               PoolingHttpClientConnectionManager connManager) {
        if (httpClientProperties != null) {
            connManager.setDefaultMaxPerRoute(httpClientProperties.getMaxConnectionsPerRoute());
            connManager.setMaxTotal(httpClientProperties.getMaxTotalConnections());
            ConnectionConfig config = ConnectionConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(httpClientProperties.getConnectTimeoutMillis()))
                    .setSocketTimeout(Timeout.ofMilliseconds(httpClientProperties.getSocketTimeoutMillis()))
                    .build();
            connManager.setDefaultConnectionConfig(config);
        }
    }
}
