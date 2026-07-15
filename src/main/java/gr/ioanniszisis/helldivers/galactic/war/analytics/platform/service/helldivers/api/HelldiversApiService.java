package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.service.helldivers.api;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.WarId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HelldiversApiService {

    private final GenericRestClient helldiversApiRestClient;
    private final HellDiversApiRestClientProperties  helldiversApiRestClientProperties;

    public BigInteger getCurrentWarId() {
        String currentWarIdUrl = helldiversApiRestClientProperties.getEndpoints().getRaw().getCurrentWarIdUrl();
        return Objects.requireNonNull(helldiversApiRestClient.get(currentWarIdUrl, WarId.class).getBody()).id();
    }
}
