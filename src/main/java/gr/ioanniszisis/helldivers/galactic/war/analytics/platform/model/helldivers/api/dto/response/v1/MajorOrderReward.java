package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The reward for completing an Assignment.
 *
 * <p>Part of {@link MajorOrder}.
 *
 * @param type   the type of reward (medals, super credits, ...)
 * @param amount the amount of Type that will be awarded
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MajorOrderReward(
		int type,
		long amount) {
}
