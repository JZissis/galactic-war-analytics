package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Represents a snapshot of the current status of the galactic war.
 *
 * <p>{@code time} is seconds since the war started. Add {@link WarInfo#startDate()} to get a unix timestamp.
 *
 * <p>Returned by {@code GET /raw/api/WarSeason/{warId}/Status}.
 *
 * @param warId            the war season this snapshot refers to
 * @param time             the time this snapshot was taken
 * @param impactMultiplier this is the factor by which influence at the end of a mission is multiplied to calculate the impact on liberation
 * @param storyBeatId32    internal identifier, purpose unknown
 * @param planetStatus     a list of statuses for planets
 * @param planetAttacks    a list of attacks currently ongoing
 * @param campaigns        a list of ongoing campaigns in the galactic war
 * @param jointOperations  a list of JointOperations
 * @param planetEvents     a list of ongoing PlanetEvents
 * @param planetRegions    the regions that have a status
 */
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
