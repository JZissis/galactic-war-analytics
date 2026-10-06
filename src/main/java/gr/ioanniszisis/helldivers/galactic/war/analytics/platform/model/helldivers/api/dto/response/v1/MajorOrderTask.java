package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Represents a task in an Assignment that needs to be completed to finish the assignment.
 *
 * <p>Part of {@link MajorOrder}.
 *
 * @param type       the type of task this represents
 * @param values     a list of numbers, purpose unknown
 * @param valueTypes a list of numbers, purpose unknown
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MajorOrderTask(
		int type,
		List<Long> values,
		List<Long> valueTypes) {
}
