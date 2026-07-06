package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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