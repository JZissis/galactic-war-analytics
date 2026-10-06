package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1.Planet;
import module java.base;

/**
 * Represents a Super Earth Democracy Space Station.
 *
 * <p>Returned by {@code GET /api/v2/space-stations} and {@code GET /api/v2/space-stations/{index}}.
 *
 * @param id32            the unique identifier of the station
 * @param planet          the planet it's currently orbiting
 * @param electionEnd     when the election for the next planet will end
 * @param flags           a set of flags, purpose currently unknown
 * @param tacticalActions a list of tactical actions the space station supports
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpaceStation(
		long id32,
		Planet planet,
		Instant electionEnd,
		int flags,
		List<TacticalAction> tacticalActions) {
}
