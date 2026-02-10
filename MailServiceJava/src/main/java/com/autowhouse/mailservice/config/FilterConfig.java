package com.autowhouse.mailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class FilterConfig {
    @Bean(name = "noFilter")
    public Set<String> noFilter(){
        Set<String> set = new HashSet<>();
        set.add("/connection/test");
        set.add("/safe/key");
        set.add("/safe/key/wow");
        set.add("/actuator");
        set.add("/actuator/health");
        set.add("/error");
        set.add("/random");
        return set;
    }
}