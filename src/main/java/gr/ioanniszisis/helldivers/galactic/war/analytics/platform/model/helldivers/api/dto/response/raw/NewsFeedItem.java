package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an item in the newsfeed of Super Earth.
 *
 * <p>The live API uses PascalCase keys, unlike its spec. {@code published} is seconds since the war started,
 * not a unix timestamp. {@link gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1.Dispatch Dispatch} carries the same items with a proper timestamp, so prefer it.
 *
 * <p>Returned by {@code GET /raw/api/NewsFeed/{warId}} (returns a list).
 *
 * @param id        the identifier of this newsfeed item
 * @param published when this item was published, in seconds since the war started
 * @param type      a numerical type, purpose unknown
 * @param message   the message containing a human readable text
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NewsFeedItem(
		@JsonProperty("Id") int id,
		@JsonProperty("Published") long published,
		@JsonProperty("Type") int type,
		@JsonProperty("Message") String message) {
}
