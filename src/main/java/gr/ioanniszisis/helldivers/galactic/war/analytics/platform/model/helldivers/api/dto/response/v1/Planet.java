package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Contains all aggregated information AH has about a planet.
 *
 * <p>Text fields are plain strings by default. Sending {@code Accept-Language: ivl-IV} makes the API
 * return every translation as an object instead, which these records do not support.
 *
 * <p>Returned by {@code GET /api/v1/planets}, {@code GET /api/v1/planets/{index}} and {@code GET /api/v1/planet-events}.
 *
 * @param index          the unique identifier ArrowHead assigned to this planet
 * @param name           the name of the planet, as shown in game
 * @param sector         the name of the sector the planet is in, as shown in game
 * @param biome          the biome this planet has
 * @param hazards        all Hazards that are applicable to this planet
 * @param hash           a hash assigned to the planet by ArrowHead, purpose unknown
 * @param position       the coordinates of this planet on the galactic war map
 * @param waypoints      a list of Index of all the planets to which this planet is connected
 * @param maxHealth      the maximum health pool of this planet
 * @param health         the current health of this planet
 * @param disabled       whether or not this planet is disabled, as assigned by ArrowHead
 * @param initialOwner   the faction that originally owned the planet
 * @param currentOwner   the faction that currently controls the planet
 * @param regenPerSecond how much the planet regenerates per second if left alone
 * @param event          information on the event ongoing on this planet, or {@code null} when there is none
 * @param statistics     a set of statistics scoped to this planet
 * @param attacking      a list of Index integers that this planet is currently attacking
 * @param regions        all regions on this planet, including their status (if any)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Planet(
		int index,
		String name,
		String sector,
		Biome biome,
		List<Hazard> hazards,
		long hash,
		Position position,
		List<Integer> waypoints,
		long maxHealth,
		long health,
		boolean disabled,
		String initialOwner,
		String currentOwner,
		double regenPerSecond,
		PlanetEventDetails event,
		Statistics statistics,
		List<Integer> attacking,
		List<Region> regions) {
}
