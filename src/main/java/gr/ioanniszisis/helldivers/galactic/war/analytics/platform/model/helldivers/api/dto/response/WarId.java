package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.model.helldivers.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WarId(
        BigInteger id
) {

}
