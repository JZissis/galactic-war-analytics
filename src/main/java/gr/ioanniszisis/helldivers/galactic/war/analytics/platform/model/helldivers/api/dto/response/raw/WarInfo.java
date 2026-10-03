package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents mostly static information of the current galactic war.
 *
 * <p>Returned by {@code GET /raw/api/WarSeason/{warId}/WarInfo}.
 *
 * @param warId                the identifier of the war season this WarInfo represents
 * @param startDate            a unix timestamp (in seconds) when this season started
 * @param endDate              a unix timestamp (in seconds) when this season will end
 * @param minimumClientVersion a version string indicating the minimum game client version the API supports
 * @param planetInfos          a list of planets involved in this season's war
 * @param homeWorlds           a list of homeworlds for the races (factions) involved in this war
 * @param planetRegions        the regions that can be found on this planet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WarInfo(
		int warId,
		long startDate,
		long endDate,
		String minimumClientVersion,
		List<PlanetInfo> planetInfos,
		List<HomeWorld> homeWorlds,
		List<PlanetRegion> planetRegions) {
}
