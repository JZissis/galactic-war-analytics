package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record War(
		Instant started,
		Instant ended,
		Instant now,
		String clientVersion,
		List<String> factions,
		double impactMultiplier,
		Statistics statistics) {
}