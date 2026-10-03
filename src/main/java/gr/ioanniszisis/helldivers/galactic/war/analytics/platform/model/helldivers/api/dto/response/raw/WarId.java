package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;

/**
 * Represents the ID returned from the WarID endpoint.
 *
 * <p>Returned by {@code GET /raw/api/WarSeason/current/WarID}.
 *
 * @param id the identifier of the current war season
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WarId(
		BigInteger id) {
}
