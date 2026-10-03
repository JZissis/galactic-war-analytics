package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

/**
 * A message from high command to the players, usually updates on the status of the war effort.
 *
 * <p>The v1 and v2 endpoints return the same shape, so one record serves both.
 * <p>Text fields are plain strings by default. Sending {@code Accept-Language: ivl-IV} makes the API
 * return every translation as an object instead, which these records do not support.
 *
 * <p>Returned by {@code GET /api/v1/dispatches}, {@code GET /api/v2/dispatches} and their /{index} variants.
 *
 * @param id        the unique identifier of this dispatch
 * @param published when the dispatch was published
 * @param type      the type of dispatch, purpose unknown
 * @param message   the message this dispatch represents
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Dispatch(
		int id,
		Instant published,
		int type,
		String message) {
}
