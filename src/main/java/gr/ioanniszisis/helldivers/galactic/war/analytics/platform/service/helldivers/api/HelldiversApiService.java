package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.service.helldivers.api;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.exception.HelldiversApiEmptyResponseException;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw.WarId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Reads Galactic War data from the Helldivers API.
 *
 * <p>Resolves endpoint paths from configuration and unwraps responses. Rate limiting, retries
 * and error mapping happen in the client layer, not here.
 */
@Service
@RequiredArgsConstructor
public class HelldiversApiService {

    private final GenericRestClient helldiversApiRestClient;
    private final HellDiversApiRestClientProperties  helldiversApiRestClientProperties;

    /**
     * Fetches the identifier of the current war season.
     *
     * @return the current war id
     * @throws HelldiversApiEmptyResponseException if the API returns no body
     */
    public BigInteger getCurrentWarId() {
        String currentWarIdUrl = helldiversApiRestClientProperties.getEndpoints().getRaw().getCurrentWarIdUrl();
        return Optional.ofNullable(helldiversApiRestClient.get(currentWarIdUrl, WarId.class).getBody())
                .orElseThrow(() -> new HelldiversApiEmptyResponseException("HellDivers API returned an empty body for the current war id"))
                .id();
    }
}
