package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WarStatus(
		int warId,
		long time,
		double impactMultiplier,
		long storyBeatId32,
		List<PlanetStatus> planetStatus,
		List<PlanetAttack> planetAttacks,
		List<Campaign> campaigns,
		List<JointOperation> jointOperations,
		List<PlanetEvent> planetEvents,
		List<PlanetRegionStatus> planetRegions) {
}
