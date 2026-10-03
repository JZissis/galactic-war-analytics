package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.net;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic connection settings for an outgoing HTTP client.
 *
 * <p>Meant to be extended by API-specific properties classes such as
 * {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties}.
 */
@Getter
@Setter
public class HttpClientProperties {

    /** Base URL that request paths are resolved against. */
    private String baseUrl;
    /** Maximum pooled connections per host. */
    private int maxConnectionsPerRoute = 200;
    /** Maximum pooled connections in total. */
    private int maxTotalConnections = 200;
    /** Time allowed to open a TCP connection, in milliseconds. */
    private long connectTimeoutMillis = 3000;
    /** Time allowed to wait for response data, in milliseconds. */
    private long readTimeoutMillis = 3000;
    /** Time allowed to get a connection from the pool, in milliseconds. */
    private long requestTimeoutMillis = 3000;
    /** Time allowed between two data packets on an open socket, in milliseconds. */
    private long socketTimeoutMillis = 3000;
    /** Name for the connection pool metrics; no metrics are registered when {@code null}. */
    private String connectionManagerMetricsTag;
}
