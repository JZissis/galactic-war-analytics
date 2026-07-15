package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client;

import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.GenericRestClient;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client.net.HttpClientFactory;
import gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.properties.HellDiversApiRestClientProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HellDiversApiRestClientConfig {

    @Bean
    public GenericRestClient helldiversApiRestClient(HellDiversApiRestClientProperties hellDiversApiRestClientProperties,
                                                     MeterRegistry meterRegistry) {

        RestClient restClient = RestClient.builder()
                .requestFactory(HttpClientFactory.createHttpClientFactory(hellDiversApiRestClientProperties, meterRegistry))
                .baseUrl(hellDiversApiRestClientProperties.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.add("X-Super-Client", hellDiversApiRestClientProperties.getSuperClient());
                    headers.add("X-Super-Contact", hellDiversApiRestClientProperties.getSuperContact());
                })
                .build();

        return new GenericRestClient(restClient);
    }

}
