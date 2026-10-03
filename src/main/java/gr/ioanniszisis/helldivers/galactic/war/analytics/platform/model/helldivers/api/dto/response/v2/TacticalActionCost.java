package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the "Cost" of a tactical action.
 *
 * <p>Part of {@link TacticalAction}. The API misspells {@code maxDonationAmount} as {@code maxDonationAmmount}.
 *
 * @param id                       the identifier of this cost
 * @param itemMixId                the identifier of the item that must be donated
 * @param targetValue              the amount needed to activate the action
 * @param currentValue             the amount donated so far
 * @param deltaPerSecond           how fast the donated amount changes per second
 * @param maxDonationAmount        the most one player can donate per period
 * @param maxDonationPeriodSeconds the length of one donation period in seconds
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TacticalActionCost(
		String id,
		long itemMixId,
		long targetValue,
		double currentValue,
		double deltaPerSecond,
		@JsonProperty("maxDonationAmmount") long maxDonationAmount,
		long maxDonationPeriodSeconds) {
}
