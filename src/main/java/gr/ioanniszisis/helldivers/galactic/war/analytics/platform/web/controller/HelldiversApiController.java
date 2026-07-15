package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.web.controller;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.service.helldivers.api.HelldiversApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/helldivers")
@RequiredArgsConstructor
public class HelldiversApiController {

    private final HelldiversApiService helldiversApiService;

    @GetMapping("/current-war-id")
    public BigInteger getCurrentWarId() {
        return helldiversApiService.getCurrentWarId();
    }
}
