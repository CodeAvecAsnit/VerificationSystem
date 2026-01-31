package com.autowhouse.mailservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;
@Component
public class APIKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "API-Key";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${app.secure.apiKey}")
    private String expectedApiKey;

    private final Set<String> publicEndpoints;

    public APIKeyFilter(@Qualifier("publicEndpoints") Set<String> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKeyHeader = request.getHeader(API_KEY_HEADER);
        if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
            sendUnauthorizedResponse(response, "Missing API key");
            return;
        }
        String apiKey = extractApiKey(apiKeyHeader);
        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            sendUnauthorizedResponse(response, "Invalid API key");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractApiKey(String header) {
        if (header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()).trim();
        }
        return header.trim();
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                String.format("{\"error\": \"%s\", \"timestamp\": \"%s\"}",
                        message, Instant.now())
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return publicEndpoints.contains(path) ||
                publicEndpoints.stream().anyMatch(path::startsWith);
    }
}