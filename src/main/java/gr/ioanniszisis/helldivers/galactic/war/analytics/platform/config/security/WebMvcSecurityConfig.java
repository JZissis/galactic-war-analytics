package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Web security configuration.
 *
 * <p>Temporary: permits every request until Phase 5 adds OAuth2 (public reads, protected admin endpoints).
 */
@Configuration
@EnableWebSecurity
public class WebMvcSecurityConfig {

    /**
     * Permissive security chain until Phase 5 introduces OAuth2 login.
     * CSRF is disabled and all requests are permitted; frames are allowed
     * from the same origin so the H2 console UI can render.
     *
     * @param http the security builder
     * @return the security filter chain
     * @throws Exception if the chain cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }
}
