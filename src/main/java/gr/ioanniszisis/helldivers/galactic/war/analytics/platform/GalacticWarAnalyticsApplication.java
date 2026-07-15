package gr.ioanniszisis.helldivers.galactic.war.analytics.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan("gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config")
public class GalacticWarAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GalacticWarAnalyticsApplication.class, args);
	}

}
