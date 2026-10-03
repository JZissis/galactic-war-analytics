package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Contains information of ongoing campaigns.
 *
 * <p>Part of {@link WarStatus}. The v1 equivalent is {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1.CampaignDetails CampaignDetails}.
 *
 * @param id          the identifier of this campaign
 * @param planetIndex the Index of the planet this campaign refers to
 * @param type        a numerical type, indicates the type of campaign (see helldivers-2/json)
 * @param count       a numerical count, the amount of campaigns the planet has seen
 * @param race        a numerical race, the race of the planet this campaign refers to
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Campaign(
		int id,
		int planetIndex,
		int type,
		long count,
		int race) {
}
