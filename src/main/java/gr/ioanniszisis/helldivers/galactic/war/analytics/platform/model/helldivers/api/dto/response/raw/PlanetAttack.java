package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an attack on a PlanetInfo.
 *
 * <p>Part of {@link WarStatus}. Both values are planet indices.
 *
 * @param source where the attack originates from
 * @param target the planet under attack
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetAttack(
		int source,
		int target) {
}
