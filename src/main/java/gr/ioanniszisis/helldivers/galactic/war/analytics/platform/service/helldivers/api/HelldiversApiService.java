package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.service.helldivers.api;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.exception.HelldiversApiEmptyResponseException;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.WarId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class HelldiversApiService {

    private final GenericRestClient helldiversApiRestClient;
    private final HellDiversApiRestClientProperties  helldiversApiRestClientProperties;

    public BigInteger getCurrentWarId() {
        String currentWarIdUrl = helldiversApiRestClientProperties.getEndpoints().getRaw().getCurrentWarIdUrl();
        WarId response = helldiversApiRestClient.get(currentWarIdUrl, WarId.class).getBody();
        if (response == null) {
            throw new HelldiversApiEmptyResponseException("HellDivers API returned an empty body for the current war id");
        }
        return response.id();
    }
}
