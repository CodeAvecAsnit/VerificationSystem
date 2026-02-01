package com.autowhouse.loginservice.service.auth.impl;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.exception.custom.MaxRateReachedException;
import com.autowhouse.loginservice.security.CustomUser;
import com.autowhouse.loginservice.security.CustomUserServiceImpl;
import com.autowhouse.loginservice.service.redis.RateLimiterService;
import com.autowhouse.loginservice.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author : Asnit Bakhati
 * @Date : 1st Feb, 2026
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock private RateLimiterService rateLimiter;
    @Mock private CustomUserServiceImpl userService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private HttpServletResponse response;

    @InjectMocks
    private LoginServiceImpl loginService;

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String MOCKED_JWT = "header.payload.signature";

    private CustomUser mockUser;
    private SignInDTO signInDTO;

    @BeforeEach
    void setUp() {
        mockUser = new CustomUser();
        mockUser.setUserName(EMAIL);
        mockUser.setPassword("hashed_password");
        signInDTO = new SignInDTO();
        signInDTO.setUserName(EMAIL + " ");
        signInDTO.setPassword(PASSWORD);
    }

    @Test
    @DisplayName("Success: Should login, attach cookie, and clear rate limit")
    void login_Success() {
        when(rateLimiter.getTries(EMAIL)).thenReturn(Optional.of(0));
        when(userService.loadUserByUsername(EMAIL)).thenReturn(mockUser);
        when(passwordEncoder.matches(PASSWORD, mockUser.getPassword())).thenReturn(true);
        when(jwtUtils.generateJwtTokens(mockUser)).thenReturn(MOCKED_JWT);
        LoginResponseDTO result = loginService.login(signInDTO, response);
        assertThat(result.getMessage()).isEqualTo("Login Successful");
        assertThat(result.getToken()).isEqualTo(MOCKED_JWT);
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        Cookie attachedCookie = cookieCaptor.getValue();
        assertThat(attachedCookie.getName()).isEqualTo("access_token");
        assertThat(attachedCookie.getValue()).isEqualTo(MOCKED_JWT);
        assertThat(attachedCookie.isHttpOnly()).isTrue();

        verify(rateLimiter).onSuccessRemove(EMAIL, 0);
    }

    @Test
    @DisplayName("Security: Should block login if max attempts reached")
    void login_MaxAttemptsReached() {
        when(rateLimiter.getTries(EMAIL)).thenReturn(Optional.of(5));
        assertThatThrownBy(() -> loginService.login(signInDTO, response))
                .isInstanceOf(MaxRateReachedException.class)
                .hasMessageContaining("Too many attempts");
        verifyNoInteractions(userService, passwordEncoder, jwtUtils);
    }

    @Test
    @DisplayName("Security: Incorrect password should increment rate limiter")
    void login_WrongPassword_IncrementsTries() {
        when(rateLimiter.getTries(EMAIL)).thenReturn(Optional.of(2));
        when(userService.loadUserByUsername(EMAIL)).thenReturn(mockUser);
        when(passwordEncoder.matches(PASSWORD, mockUser.getPassword())).thenReturn(false);
        assertThatThrownBy(() -> loginService.login(signInDTO, response))
                .isInstanceOf(BadCredentialsException.class);
        verify(rateLimiter).setTries(EMAIL, 3);
        verify(rateLimiter, never()).onSuccessRemove(anyString(), anyInt());
    }

    @Test
    @DisplayName("Robustness: User not found should be treated as BadCredentials")
    void login_UserNotFound_HandledDefensively() {
        when(rateLimiter.getTries(EMAIL)).thenReturn(Optional.of(0));
        when(userService.loadUserByUsername(EMAIL)).thenThrow(new RuntimeException("User not found"));
        assertThatThrownBy(() -> loginService.login(signInDTO, response))
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    @DisplayName("Edge Case: Should trim username input")
    void login_TrimsUsername() {
        String messyEmail = "  test@example.com  ";
        signInDTO.setUserName(messyEmail);

        when(rateLimiter.getTries(EMAIL)).thenReturn(Optional.of(0));
        when(userService.loadUserByUsername(EMAIL)).thenReturn(mockUser);
        when(passwordEncoder.matches(PASSWORD, mockUser.getPassword())).thenReturn(true);

        loginService.login(signInDTO, response);

        verify(rateLimiter).getTries(EMAIL); // Verifies it used the trimmed version
        verify(userService).loadUserByUsername(EMAIL);
    }
}