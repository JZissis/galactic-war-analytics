package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.net;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpClientProperties {

    private String baseUrl;
    private int maxConnectionsPerRoute = 200;
    private int maxTotalConnections = 200;
    private long connectTimeoutMillis = 3000;
    private long readTimeoutMillis = 3000;
    private long requestTimeoutMillis = 3000;
    private long socketTimeoutMillis = 3000;
    private String connectionManagerMetricsTag;
}
