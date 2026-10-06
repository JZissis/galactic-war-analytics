package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Represents an assignment given by Super Earth to the community. This is also known as 'Major Order's
 * in the game.
 *
 * <p>The raw equivalent is {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw.Assignment Assignment}.
 * <p>Text fields are plain strings by default. Sending {@code Accept-Language: ivl-IV} makes the API
 * return every translation as an object instead, which these records do not support.
 *
 * <p>Returned by {@code GET /api/v1/assignments} and {@code GET /api/v1/assignments/{index}}.
 *
 * @param id          the unique identifier of this assignment
 * @param progress    a list of numbers, how they represent progress is unknown
 * @param title       the title of the assignment
 * @param briefing    a long form description of the assignment, usually contains context
 * @param description a very short summary of the description
 * @param tasks       a list of tasks that need to be completed for this assignment
 * @param reward      the reward for completing the assignment, may be {@code null}
 * @param rewards     a list of rewards for completing the assignment
 * @param expiration  the date when the assignment will expire
 * @param flags       flags regarding the assignment
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MajorOrder(
		long id,
		List<Long> progress,
		String title,
		String briefing,
		String description,
		List<MajorOrderTask> tasks,
		MajorOrderReward reward,
		List<MajorOrderReward> rewards,
		Instant expiration,
		int flags) {
}
