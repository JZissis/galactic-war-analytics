package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Biome(String name, String description) {
}