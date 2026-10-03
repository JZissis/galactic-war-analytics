package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Describes an environmental hazard that can be present on a Planet.
 *
 * <p>Part of {@link Planet}.
 *
 * @param name        the name of this environmental hazard
 * @param description the description of the environmental hazard
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Hazard(
		String name,
		String description) {
}
