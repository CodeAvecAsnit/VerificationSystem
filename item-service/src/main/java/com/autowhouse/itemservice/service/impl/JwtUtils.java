package com.autowhouse.itemservice.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : Asnit Bakhati
 * @Date : 9th March 2026
 */

@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    /**
     * Extract roles from JWT token
     * IMPROVED: Handles both "roles" (list) and "role" (single) for backward compatibility
     */
    public List<String> getUserRolesFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Object rolesObj = claims.get("roles");
            if (rolesObj != null) {
                if (rolesObj instanceof List<?>) {
                    List<String> roles = new ArrayList<>();
                    for (Object role : (List<?>) rolesObj) {
                        if (role != null) {
                            roles.add(role.toString());
                        }
                    }
                    log.debug("Extracted {} roles from token", roles.size());
                    return roles;
                } else if (rolesObj instanceof String) {
                    log.debug("Extracted single role from 'roles' claim");
                    return List.of((String) rolesObj);
                }
            }
            Object roleObj = claims.get("role");
            if (roleObj instanceof String) {
                log.debug("Extracted single role from 'role' claim (backward compatibility)");
                return List.of((String) roleObj);
            }
            log.warn("No roles found in JWT token");
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("Error extracting roles from token: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Object idObj = claims.get("id");
            if (idObj instanceof Integer) {
                return ((Integer) idObj).longValue();
            } else if (idObj instanceof Long) {
                return (Long) idObj;
            }
            log.warn("Could not extract user ID from token");
            return null;
        } catch (Exception e) {
            log.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    public String getUserNameFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}