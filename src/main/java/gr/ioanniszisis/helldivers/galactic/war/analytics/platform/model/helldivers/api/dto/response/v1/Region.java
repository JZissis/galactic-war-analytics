package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import module java.base;

/**
 * A region on a planet. The Name and Description fields may be empty when the underlying data store
 * doesn't contain information on them. This is typically when ArrowHead adds new regions that aren't
 * updated in the data store (helldivers-2/json) yet. Note that some properties may be unavailable when
 * the region is inactive.
 *
 * <p>Part of {@link Planet}. Known {@code size} values: {@code Settlement}, {@code Town}, {@code City},
 * {@code MegaCity}. Nullable numbers are boxed. {@code hash} is an unsigned 64-bit value, so it is a
 * {@link BigInteger}.
 *
 * @param id                 the identifier of this region
 * @param hash               the underlying hash identifier of ArrowHead
 * @param name               the name of the region
 * @param description        a long-form description of the region
 * @param health             the current health of the region
 * @param maxHealth          the maximum health of this region
 * @param size               the size of this region
 * @param regenPerSecond     the amount of health this region generates when left alone
 * @param availabilityFactor unknown purpose
 * @param isAvailable        whether the region is currently playable(?)
 * @param players            the amount of helldivers currently active in this region
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Region(
		int id,
		BigInteger hash,
		String name,
		String description,
		Long health,
		long maxHealth,
		String size,
		Double regenPerSecond,
		Double availabilityFactor,
		boolean isAvailable,
		long players) {
}
