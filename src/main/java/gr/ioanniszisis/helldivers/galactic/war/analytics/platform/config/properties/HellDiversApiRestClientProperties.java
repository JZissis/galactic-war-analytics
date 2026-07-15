package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.net.HttpClientProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "helldivers-api-rest")
public class HellDiversApiRestClientProperties extends HttpClientProperties {

    private String superClient;
    private String superContact;
    private EndpointsProperties endpoints;

    @Getter
    @Setter
    public static class EndpointsProperties {
        private RawEndpointsProperties raw;
        private V1EndpointsProperties v1;
        private V2EndpointsProperties v2;
    }

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

    @Getter
    @Setter
    public static class V2EndpointsProperties {
        private String availableDispatchesUrl;
        private String spaceStationInfoUrl;
    }
}
