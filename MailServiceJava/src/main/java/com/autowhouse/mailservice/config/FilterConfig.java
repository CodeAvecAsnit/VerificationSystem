package com.autowhouse.mailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class FilterConfig {

    @Bean(name = "publicEndpoints")
    public Set<String> publicEndpoints() {
        return Set.of(
                "/connection/test", "/safe/key", "/safe/key/wow", "/actuator/health", "/error");
    }
}