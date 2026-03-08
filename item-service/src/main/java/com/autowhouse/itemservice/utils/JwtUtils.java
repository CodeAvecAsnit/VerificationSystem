package com.autowhouse.itemservice.utils;

import com.autowhouse.itemservice.config.CustomUser;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.auth-expiration}")
    private long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        log.info(bearerToken);
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        else return null;
    }

    public String generateJwtTokens(CustomUser userDetails){
        String username = userDetails.getUsername();
        String detailsRole = userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("USER_ROLE");
        return Jwts.builder().subject(username).claim("role",detailsRole).claim("id",userDetails.getId()).
                issuedAt(new Date()).
                expiration(new Date(new Date().getTime()+jwtExpiration)).
                signWith(secretKey).
                compact();
    }

    public int getUserIdFromToken(String token){
        return Jwts.parser().
                verifyWith(secretKey).
                build().parseSignedClaims(token).
                getPayload().get("id",Integer.class);
    }


    public String getUserRoleFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }


    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(authToken);
            return true;
        }catch (Exception ex){
            log.error("Problem with the token");
        }
        return false;
    }
}

