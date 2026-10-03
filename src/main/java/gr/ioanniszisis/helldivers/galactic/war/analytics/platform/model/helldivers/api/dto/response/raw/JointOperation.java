package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a joint operation.
 *
 * <p>Part of {@link WarStatus}.
 *
 * @param id          the identifier of this joint operation
 * @param planetIndex the index of the planet this joint operation takes place on
 * @param hqNodeIndex the index of the headquarters node, purpose unknown
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record JointOperation(
		int id,
		int planetIndex,
		int hqNodeIndex) {
}
