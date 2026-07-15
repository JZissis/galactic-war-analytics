package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetRegionStatus(
		int planetIndex,
		int regionIndex,
		int owner,
		long health,
		@JsonProperty("regerPerSecond") double regenPerSecond,
		double availabilityFactor,
		boolean isAvailable,
		long players) {
}
