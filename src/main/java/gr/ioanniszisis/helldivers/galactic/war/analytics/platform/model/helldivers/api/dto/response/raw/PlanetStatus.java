package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the 'current' status of a planet in the galactic war.
 *
 * <p>Part of {@link WarStatus}.
 *
 * @param index          the identifier of the PlanetInfo this status refers to
 * @param owner          the faction currently owning the planet
 * @param health         the current health / liberation of a planet
 * @param regenPerSecond if left alone, how much the health of the planet would regenerate
 * @param players        the amount of helldivers currently active on this planet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetStatus(
		int index,
		int owner,
		long health,
		double regenPerSecond,
		long players) {
}
