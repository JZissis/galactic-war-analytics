package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the reward of an Assignment.
 *
 * <p>Part of {@link AssignmentSetting}.
 *
 * @param type   the type of reward, currently only one value is known: 1 which represents Medals
 * @param id32   internal identifier of this Reward
 * @param amount the amount of Type the players will receive upon completion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssignmentReward(
		int type,
		long id32,
		long amount) {
}
