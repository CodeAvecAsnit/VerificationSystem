package com.autowhouse.loginservice.util;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.enumeration.TokenStatus;
import com.autowhouse.loginservice.security.CustomUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.auth-expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-threshold}")
    private long refreshThresholdMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Generate JWT token from UserDetailsImpl (for login)
     * FIXED: Now uses "roles" (plural) to be consistent
     */
    public String generateJwtTokens(CustomUser customUser){
        String username = customUser.getUsername();

        List<String> roles = customUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("id", customUser.getUserId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generate access token from BaseAccount (for refresh)
     */
    public String generateAccessTokenFromUser(ApplicationUser user) {
        List<String> roleNames = user.getUserRoles().stream()
                .map(r -> r.getRole().name())
                .toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roleNames)
                .claim("id", user.getRegisterId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
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

    public Long getUserIdFromToken(String token){
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

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public TokenStatus checkTokenStatus(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiryDate = claims.getExpiration();
            long timeUntilExpiry = expiryDate.getTime() - System.currentTimeMillis();

            if (timeUntilExpiry <= 0) {
                return TokenStatus.EXPIRED;
            } else if (timeUntilExpiry < refreshThresholdMs) {
                return TokenStatus.EXPIRING_SOON;
            } else {
                return TokenStatus.VALID;
            }
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (SignatureException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            log.error("Token validation error: {}", e.getMessage());
            return TokenStatus.INVALID;
        }
    }

    public long getExpirationTime(){
        return this.jwtExpiration;
    }

    /**
     * Debug method to print all claims in a token (useful for troubleshooting)
     */
    public void debugTokenClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("=== JWT Token Claims Debug ===");
            log.debug("Subject: {}", claims.getSubject());
            log.debug("Issued At: {}", claims.getIssuedAt());
            log.debug("Expiration: {}", claims.getExpiration());
            claims.forEach((key, value) ->
                    log.debug("Claim '{}': {} (type: {})", key, value, value != null ? value.getClass().getSimpleName() : "null")
            );
            log.debug("==============================");
        } catch (Exception e) {
            log.error("Error debugging token claims: {}", e.getMessage());
        }
    }
}