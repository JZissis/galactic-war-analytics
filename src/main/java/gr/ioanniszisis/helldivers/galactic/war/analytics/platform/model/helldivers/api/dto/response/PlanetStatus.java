package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetStatus(
		int index,
		int owner,
		long health,
		double regenPerSecond,
		long players) {
}
