package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * Represents a tactical action that the Space Station can take.
 *
 * <p>Part of {@link SpaceStation}.
 *
 * @param id32                 the identifier of this tactical action
 * @param mediaId32            the identifier of the media (icon) shown for this action
 * @param name                 the name of this tactical action
 * @param description          a description of what this action does
 * @param strategicDescription a description of the strategic effect of this action
 * @param status               the numerical status of this action, values unknown
 * @param statusExpire         when the current status expires
 * @param costs                what players must donate to activate this action
 * @param effectIds            the identifiers of the effects this action applies
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TacticalAction(
		long id32,
		long mediaId32,
		String name,
		String description,
		String strategicDescription,
		int status,
		Instant statusExpire,
		List<TacticalActionCost> costs,
		List<Integer> effectIds) {
}
