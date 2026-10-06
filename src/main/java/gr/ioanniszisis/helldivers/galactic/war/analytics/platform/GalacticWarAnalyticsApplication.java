package gr.ioanniszisis.helldivers.galactic.war.analytics.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point of the Galactic War Analytics Platform.
 *
 * <p>The application pulls Galactic War data from the community Helldivers 2 API
 * ({@code api.helldivers2.dev}) and exposes it through its own REST API. Configuration
 * properties classes under the {@code config} package are picked up automatically.
 */
@SpringBootApplication
@ConfigurationPropertiesScan("gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config")
public class GalacticWarAnalyticsApplication {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments passed to Spring Boot
	 */
	static void main(String[] args) {
		SpringApplication.run(GalacticWarAnalyticsApplication.class, args);
	}

}
