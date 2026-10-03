package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents information about the homeworld(s) of a given race.
 *
 * <p>Part of {@link WarInfo}.
 *
 * @param race          the identifier of the race (faction) this describes the homeworld of
 * @param planetIndices the indices of the planets that are this race's homeworlds
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record HomeWorld(
		int race,
		List<Integer> planetIndices) {
}
