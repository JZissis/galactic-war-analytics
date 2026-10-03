package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a position on the galactic war map.
 *
 * <p>Part of {@link Planet}.
 *
 * @param x the X coordinate
 * @param y the Y coordinate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Position(
		double x,
		double y) {
}
