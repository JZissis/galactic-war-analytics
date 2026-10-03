package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Contains the details of an Assignment like reward and requirements.
 *
 * <p>Part of {@link Assignment}.
 *
 * @param type            the type of assignment, values unknown at the moment
 * @param overrideTitle   the title of this assignment
 * @param overrideBrief   the briefing (description) of this assignment
 * @param taskDescription a description of what is expected of Helldivers to complete the assignment
 * @param tasks           a list of Tasks describing the assignment requirements
 * @param reward          the reward players receive on completion, may be {@code null}
 * @param rewards         contains information on the rewards players will receive upon completion
 * @param flags           flags, suspected to be a binary OR'd value, purpose unknown
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssignmentSetting(
		int type,
		String overrideTitle,
		String overrideBrief,
		String taskDescription,
		List<AssignmentTask> tasks,
		AssignmentReward reward,
		List<AssignmentReward> rewards,
		int flags) {
}
