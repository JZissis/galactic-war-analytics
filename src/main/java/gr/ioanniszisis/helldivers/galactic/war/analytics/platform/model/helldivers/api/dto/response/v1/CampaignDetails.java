package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an ongoing campaign on a planet.
 *
 * <p>The raw equivalent is {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw.Campaign Campaign}.
 *
 * <p>Returned by {@code GET /api/v1/campaigns} and {@code GET /api/v1/campaigns/{index}}.
 *
 * @param id      the unique identifier of this Campaign
 * @param planet  the planet on which this campaign is being fought
 * @param type    the type of campaign, this should be mapped onto an enum
 * @param count   indicates how many campaigns have already been fought on this Planet
 * @param faction the faction that is currently fighting this campaign
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CampaignDetails(
		int id,
		Planet planet,
		int type,
		long count,
		String faction) {
}
