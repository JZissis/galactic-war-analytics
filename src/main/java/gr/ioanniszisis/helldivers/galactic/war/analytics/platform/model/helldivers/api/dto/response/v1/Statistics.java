package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Contains statistics of missions, kills, success rate etc.
 *
 * <p>Part of {@link War} and {@link Planet}.
 *
 * @param missionsWon        the amount of missions won
 * @param missionsLost       the amount of missions lost
 * @param missionTime        the total amount of time spent planetside (in seconds)
 * @param terminidKills      the total amount of bugs killed since start of the season
 * @param automatonKills     the total amount of automatons killed since start of the season
 * @param illuminateKills    the total amount of Illuminate killed since start of the season
 * @param bulletsFired       the total amount of bullets fired
 * @param bulletsHit         the total amount of bullets hit
 * @param timePlayed         the total amount of time played (including off-planet) in seconds
 * @param deaths             the amount of casualties on the side of humanity
 * @param revives            the amount of revives(?)
 * @param friendlies         the amount of friendly fire casualties
 * @param missionSuccessRate a percentage indicating how many started missions end in success
 * @param accuracy           a percentage indicating average accuracy of Helldivers
 * @param playerCount        the total amount of players present (at the time of the snapshot)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Statistics(
		long missionsWon,
		long missionsLost,
		long missionTime,
		long terminidKills,
		long automatonKills,
		long illuminateKills,
		long bulletsFired,
		long bulletsHit,
		long timePlayed,
		long deaths,
		long revives,
		long friendlies,
		long missionSuccessRate,
		long accuracy,
		long playerCount) {
}
