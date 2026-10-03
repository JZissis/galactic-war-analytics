package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents information about a biome of a planet.
 *
 * <p>Part of {@link Planet}.
 *
 * @param name        the name of this biome
 * @param description a human-readable description of the biome
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Biome(
		String name,
		String description) {
}
