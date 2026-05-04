package com.garmentfactory.gateway.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties({GatewayCorsProperties.class, GatewayJwtProperties.class})
public class GatewayWebFluxConfiguration {

    @Bean
    @ConditionalOnProperty(name = "gateway.cors.enabled", havingValue = "true")
    public CorsWebFilter gatewayCorsWebFilter(GatewayCorsProperties properties) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(properties.isAllowCredentials());
        config.setAllowedOrigins(properties.getAllowedOrigins());
        config.setAllowedMethods(properties.getAllowedMethods());
        config.setAllowedHeaders(properties.getAllowedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
