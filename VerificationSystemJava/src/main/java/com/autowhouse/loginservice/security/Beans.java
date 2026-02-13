package com.autowhouse.loginservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
@Configuration
public class Beans {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public SecureRandom secureRandom(){
        return new SecureRandom();
    }
}
