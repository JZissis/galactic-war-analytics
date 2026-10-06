package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Represents information of a planet from the 'WarInfo' endpoint returned by ArrowHead's API.
 *
 * <p>Part of {@link WarInfo}.
 *
 * @param index        the numerical identifier for this planet, used as reference by other properties throughout the API (like Waypoints)
 * @param settingsHash purpose unknown at this time
 * @param position     a set of X/Y coordinates specifying the position of this planet on the galaxy map
 * @param waypoints    a list of links to other planets (supply lines)
 * @param sector       the identifier of the sector this planet is located in
 * @param maxHealth    the 'health' of this planet, indicates how much liberation it needs to switch sides
 * @param disabled     whether this planet is currently considered active in the galactic war
 * @param initialOwner the identifier of the faction that initially owned this planet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetInfo(
		int index,
		long settingsHash,
		PlanetCoordinates position,
		List<Integer> waypoints,
		int sector,
		long maxHealth,
		boolean disabled,
		int initialOwner) {
}
