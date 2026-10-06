package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Global information of the ongoing war.
 *
 * <p>{@code now} is wrong in the live API (it shows 1972). The API reads in-game seconds as a unix time,
 * so do not use it as a snapshot timestamp.
 *
 * <p>Returned by {@code GET /api/v1/war}.
 *
 * @param started          when this war was started
 * @param ended            when this war will end (or has ended)
 * @param now              the time the snapshot of the war was taken, also doubles as the timestamp of which all other data dates from
 * @param clientVersion    the minimum game client version required to play in this war
 * @param factions         a list of factions currently involved in the war
 * @param impactMultiplier a fraction used to calculate the impact of a mission on the war effort
 * @param statistics       the statistics available for the galaxy wide war effort
 */
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
