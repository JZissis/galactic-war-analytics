package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

/**
 * Represents a new article from Steam's news feed.
 *
 * <p>Returned by {@code GET /api/v1/steam} and {@code GET /api/v1/steam/{gid}}.
 *
 * @param id          the identifier assigned by Steam to this news item
 * @param title       the title of the Steam news item
 * @param url         the URL to Steam where this news item was posted
 * @param author      the author who posted this message on Steam
 * @param content     the message posted by Steam, currently in Steam's weird markdown format
 * @param publishedAt when this message was posted
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SteamNews(
		String id,
		String title,
		String url,
		String author,
		String content,
		Instant publishedAt) {
}
