package com.garmentfactory.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "gateway.cors")
public class GatewayCorsProperties {

    /**
     * When true, registers a {@link org.springframework.web.cors.reactive.CorsWebFilter} for the gateway.
     */
    private boolean enabled = false;

    private List<String> allowedOrigins = new ArrayList<>();

    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

    private List<String> allowedHeaders = List.of("*");

    private boolean allowCredentials = true;
}
