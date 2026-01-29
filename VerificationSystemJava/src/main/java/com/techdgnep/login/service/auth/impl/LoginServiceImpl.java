package com.techdgnep.login.service.auth.impl;

import com.techdgnep.login.data.dto.LoginResponseDTO;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.exception.MaxRateReachedException;
import com.techdgnep.login.security.CustomUser;
import com.techdgnep.login.security.CustomUserServiceImpl;
import com.techdgnep.login.service.auth.LoginService;
import com.techdgnep.login.service.redis.RateLimiterService;
import com.techdgnep.login.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    private static final int MAX_ATTEMPTS = 5;

    private final RateLimiterService rateLimiter;
    private final CustomUserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public LoginServiceImpl(RateLimiterService rateLimiter,
                            CustomUserServiceImpl userService,
                            PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.rateLimiter = rateLimiter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponseDTO login(SignInDTO signIn, HttpServletResponse httpResponse) {
        String identifier = signIn.getUserName().trim();
        int currentTries = validateRateLimit(identifier);
        try {
            CustomUser user = userService.loadUserByUsername(identifier);
            if (!passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
                handleFailedLogin(identifier,currentTries);
            }
            rateLimiter.onSuccessRemove(identifier,currentTries);
            log.info("User {} successfully authenticated", identifier);
            String jwt = jwtUtils.generateJwtTokens(user);
            attachJwt(httpResponse,jwt);
            return new LoginResponseDTO("Authentication successful", jwt);
        } catch (Exception e) {
            log.error("Login error for user {}: {}", identifier, e.getMessage());
            handleFailedLogin(identifier, currentTries);
        }
        return null;
    }

    public void handleFailedLogin(String email, int tries){
        rateLimiter.setTries(email,++tries);
        throw new BadCredentialsException("Credentials don't match");
    }

    private int validateRateLimit(String identifier) {
        int tries = rateLimiter.getTries(identifier).orElse(0);
        if (tries >= MAX_ATTEMPTS) {
            log.warn("Brute force protection triggered for: {}", identifier);
            throw new MaxRateReachedException("Too many attempts. Please try again in 15 minutes.");
        }
        return tries;
    }

    private void attachJwt(HttpServletResponse response, String jwt){
        Cookie cookie = new Cookie("access_token",jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);//set true in production through https
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }
}