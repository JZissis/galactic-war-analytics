package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Represents an assignment given from Super Earth to the Helldivers.
 *
 * <p>The v1 equivalent is {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1.MajorOrder MajorOrder}.
 *
 * <p>Returned by {@code GET /raw/api/v2/Assignment/War/{warId}} (returns a list).
 *
 * @param id32      internal identifier of this assignment
 * @param progress  a list of numbers, how they represent progress is unknown
 * @param expiresIn the amount of seconds until this assignment expires
 * @param setting   contains detailed information on this assignment like briefing, rewards, 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Assignment(
		long id32,
		List<Long> progress,
		long expiresIn,
		AssignmentSetting setting) {
}
