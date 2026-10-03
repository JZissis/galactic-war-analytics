package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents a task in an Assignment. It's exact values are not known, therefore little of it's
 * purpose is clear.
 *
 * <p>Part of {@link AssignmentSetting}.
 *
 * @param type       a numerical value, purpose unknown
 * @param values     a list of numerical values, purpose unknown
 * @param valueTypes a list of numerical values, purpose unknown
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssignmentTask(
		int type,
		List<Long> values,
		List<Long> valueTypes) {
}
