package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import module java.base;

/**
 * Gets general statistics about the galaxy and specific planets.
 *
 * <p>The API uses snake_case names for these two fields.
 *
 * <p>Returned by {@code GET /raw/api/Stats/war/{warId}/summary}.
 *
 * @param galaxyStats  contains galaxy wide statistics aggregated from all planets
 * @param planetsStats contains statistics for specific planets
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WarSummary(
		@JsonProperty("galaxy_stats") GalaxyStats galaxyStats,
		@JsonProperty("planets_stats") List<PlanetStats> planetsStats) {
}
