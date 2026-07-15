package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Campaign(
		int id,
		int planetIndex,
		int type,
		long count,
		int race) {
}
