package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetEvent(
		int id,
		int planetIndex,
		int eventType,
		int race,
		long health,
		long maxHealth,
		long startTime,
		long expireTime,
		int campaignId,
		List<Integer> jointOperationIds) {
}
