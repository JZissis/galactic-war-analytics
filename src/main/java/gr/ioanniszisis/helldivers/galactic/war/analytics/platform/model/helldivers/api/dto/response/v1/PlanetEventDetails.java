package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;

/**
 * An ongoing event on a Planet.
 *
 * <p>Part of {@link Planet}. The raw equivalent is {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw.PlanetEvent PlanetEvent}.
 *
 * @param id                the unique identifier of this event
 * @param eventType         the type of event
 * @param faction           the faction that initiated the event
 * @param health            the health of the Event at the time of snapshot
 * @param maxHealth         the maximum health of the Event at the time of snapshot
 * @param startTime         when the event started
 * @param endTime           when the event will end
 * @param campaignId        the identifier of the Campaign linked to this event
 * @param jointOperationIds the identifiers of the joint operations linked to this event
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetEventDetails(
		int id,
		int eventType,
		String faction,
		long health,
		long maxHealth,
		Instant startTime,
		Instant endTime,
		int campaignId,
		List<Integer> jointOperationIds) {
}
