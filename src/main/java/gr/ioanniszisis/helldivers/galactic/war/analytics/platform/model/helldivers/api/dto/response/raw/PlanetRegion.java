package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * A region of a planet, containing information about its health and size.
 *
 * <p>Part of {@link WarInfo}. {@code settingsHash} is an unsigned 64-bit value, so it is a {@link BigInteger}.
 *
 * @param planetIndex  the index of the region's planet
 * @param regionIndex  the index of the region
 * @param settingsHash the ID that identifies the region in the JSON files
 * @param maxHealth    the maximum health of this region
 * @param regionSize   the size of the region
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetRegion(
		int planetIndex,
		int regionIndex,
		BigInteger settingsHash,
		long maxHealth,
		int regionSize) {
}
