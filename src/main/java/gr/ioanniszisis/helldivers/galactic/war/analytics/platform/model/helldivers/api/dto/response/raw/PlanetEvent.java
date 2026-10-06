package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * An ongoing event on a planet.
 *
 * <p>Part of {@link WarStatus}. Times are seconds since the war started. The v1 equivalent is {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1.PlanetEventDetails PlanetEventDetails}.
 *
 * @param id                the unique identifier of this event
 * @param planetIndex       the index of the planet
 * @param eventType         a numerical identifier that indicates what type of event this is
 * @param race              the identifier of the faction that owns the planet currently
 * @param health            the current health of the event
 * @param maxHealth         the current maximum health of the event
 * @param startTime         when this event started
 * @param expireTime        when the event will end
 * @param campaignId        the unique identifier of a related campaign
 * @param jointOperationIds a list of identifiers of related joint operations
 */
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
