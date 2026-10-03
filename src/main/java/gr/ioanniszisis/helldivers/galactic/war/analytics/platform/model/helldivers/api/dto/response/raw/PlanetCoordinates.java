package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a set of coordinates returned by ArrowHead's API.
 *
 * <p>Part of {@link PlanetInfo}.
 *
 * @param x the X coordinate
 * @param y the Y coordinate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetCoordinates(
		double x,
		double y) {
}
