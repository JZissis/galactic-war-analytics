package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the 'current' status of a planet's region in the galactic war.
 *
 * <p>Part of {@link WarStatus}. The API misspells {@code regenPerSecond} as {@code regerPerSecond}.
 *
 * @param planetIndex        the identifier of the PlanetInfo this region is on
 * @param regionIndex        the identifier of the PlanetRegion this data refers to
 * @param owner              the current faction that controls the region
 * @param health             the current health / liberation of the region
 * @param regenPerSecond     if left alone, how much the health of the region would regenerate
 * @param availabilityFactor unknown purpose
 * @param isAvailable        whether this region is currently available to play on(?)
 * @param players            the amount of helldivers currently active on this planet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetRegionStatus(
		int planetIndex,
		int regionIndex,
		int owner,
		long health,
		@JsonProperty("regerPerSecond") double regenPerSecond,
		double availabilityFactor,
		boolean isAvailable,
		long players) {
}
