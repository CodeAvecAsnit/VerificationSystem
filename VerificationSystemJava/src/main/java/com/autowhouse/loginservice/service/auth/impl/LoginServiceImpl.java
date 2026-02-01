package com.autowhouse.loginservice.service.auth.impl;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.exception.custom.MaxRateReachedException;
import com.autowhouse.loginservice.security.CustomUser;
import com.autowhouse.loginservice.security.CustomUserServiceImpl;
import com.autowhouse.loginservice.service.auth.LoginService;
import com.autowhouse.loginservice.service.redis.RateLimiterService;
import com.autowhouse.loginservice.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private static final int MAX_ATTEMPTS = 5;
    private static final String COOKIE_NAME = "access_token";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE_SECONDS = 86400;

    private final RateLimiterService rateLimiter;
    private final CustomUserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public LoginServiceImpl(RateLimiterService rateLimiter,
                            CustomUserServiceImpl userService,
                            PasswordEncoder passwordEncoder,
                            JwtUtils jwtUtils) {
        this.rateLimiter = rateLimiter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponseDTO login(SignInDTO signIn, HttpServletResponse httpResponse) {
        try{
        String identifier = signIn.getUserName().trim();
        int currentTries = validateRateLimit(identifier);
        CustomUser user = userService.loadUserByUsername(identifier);
        if (!passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
            handleFailedLogin(identifier, currentTries, "Username or password don't match");
        }
        rateLimiter.onSuccessRemove(identifier, currentTries);
        log.info("User {} successfully authenticated", identifier);
        String jwt = jwtUtils.generateJwtTokens(user);
        attachJwt(httpResponse, jwt);
        return new LoginResponseDTO(jwt,"Login Successful");
        }catch (BadCredentialsException | MaxRateReachedException be){
            throw be;
        }catch (RuntimeException mEx){
            throw new RuntimeException("Something unexpected occurred. Please try again later.");
        }
    }

    private void handleFailedLogin(String email, int tries,String msg){
        rateLimiter.setTries(email,++tries);
        throw new BadCredentialsException(msg);
    }

    private int validateRateLimit(String identifier) {
        int tries = rateLimiter.getTries(identifier).orElse(0);
        if (tries >= MAX_ATTEMPTS) {
            log.warn("Brute force protection triggered for: {}", identifier);
            throw new MaxRateReachedException("Too many attempts. Please try again later.");
        }
        return tries;
    }

    private void attachJwt(HttpServletResponse response, String jwt){
        Cookie cookie = new Cookie(COOKIE_NAME,jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);//set true in production through https
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        response.addCookie(cookie);
    }
}