package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.net.HttpClientProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Settings for the Helldivers API client, bound from the {@code helldivers-api-rest} prefix.
 *
 * <p>Adds the identification headers and endpoint paths on top of the generic
 * {@link HttpClientProperties}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "helldivers-api-rest")
public class HellDiversApiRestClientProperties extends HttpClientProperties {

    /** Value of the {@code X-Super-Client} header: the name of this application. */
    private String superClient;
    /** Value of the {@code X-Super-Contact} header: how the API maintainers can reach us. */
    private String superContact;
    /** Endpoint paths, grouped by API version. */
    private EndpointsProperties endpoints;

    /** Endpoint paths grouped by API version. */
    @Getter
    @Setter
    public static class EndpointsProperties {
        private RawEndpointsProperties raw;
        private V1EndpointsProperties v1;
        private V2EndpointsProperties v2;
    }

    /** Paths of the {@code /raw} endpoints, which proxy ArrowHead's own API and return its payloads as-is. */
    @Getter
    @Setter
    public static class RawEndpointsProperties {
        private String currentWarIdUrl;
        private String currentWarStatusUrl;
        private String currentWarInfoUrl;
        private String currentWarSummaryUrl;
        private String newsFeedUrl;
        private String activeAssignmentsUrl;
        private String spaceStationInfoUrl;
    }

    /** Paths of the community {@code /api/v1} endpoints, which return cleaned-up, aggregated payloads. */
    @Getter
    @Setter
    public static class V1EndpointsProperties {
        private String currentWarStateUrl;
        private String availableAssignmentsUrl;
        private String availableCampaignsUrl;
        private String availableDispatchesUrl;
        private String planetInfoUrl;
        private String planetEventsUrl;
        private String steamNewsFeedUrl;
    }

    /** Paths of the community {@code /api/v2} endpoints. */
    @Getter
    @Setter
    public static class V2EndpointsProperties {
        private String availableDispatchesUrl;
        private String spaceStationInfoUrl;
    }
}
