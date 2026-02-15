package com.autowhouse.loginservice.service.auth.impl;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import com.autowhouse.loginservice.exception.custom.UserAlreadyExistsException;
import com.autowhouse.loginservice.service.application.UserService;
import com.autowhouse.loginservice.service.kafka.KafkaCodePublisher;
import com.autowhouse.loginservice.service.redis.RedisTokenStorage;
import com.autowhouse.loginservice.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author : Asnit Bakhati
 * @Date : 15th Feb, 2026
 */
@ExtendWith(MockitoExtension.class)
class SignInServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private KafkaCodePublisher kafkaCodePublisher;

    @Mock
    private RedisTokenStorage redisTokenStorage;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SignInServiceImpl signInService;

    @Test
    @DisplayName("VerifyStoreAndSend - Fail: User already exists")
    void verifyStoreAndSend_ShouldThrowException_WhenUserExists() {
        SignInDTO dto = new SignInDTO("test@example.com", "password");
        when(userService.findByEmail(dto.getEmail())).thenReturn(Optional.of(new ApplicationUser()));
        assertThrows(UserAlreadyExistsException.class, () -> signInService.verifyStoreAndSend(dto));
        verifyNoInteractions(redisTokenStorage, kafkaCodePublisher);
    }

    @Test
    @DisplayName("VerifyStoreAndSend - Success: Store in Redis and Publish to Kafka")
    void verifyStoreAndSend_ShouldSucceed_WhenUserIsNew() {
        SignInDTO dto = new SignInDTO("new@example.com", "password");
        VerificationDTO vDto = new VerificationDTO("new@example.com", 123456);

        when(userService.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(redisTokenStorage.generateAndStore(dto)).thenReturn(vDto);

        signInService.verifyStoreAndSend(dto);

        verify(redisTokenStorage).generateAndStore(dto);
        verify(kafkaCodePublisher).publishEmail(vDto);
    }

    @Test
    @DisplayName("VerifyCode - Success: Creates user and attaches JWT")
    void verifyCode_ShouldReturnResponse_WhenCodeIsValid() {
        VerificationDTO vDto = new VerificationDTO("test@example.com", 123456);
        DetailsCodeDTO detailsDto = new DetailsCodeDTO();
        ApplicationUser savedUser = new ApplicationUser();
        String mockJwt = "jwt-token";

        when(redisTokenStorage.checkStorage(vDto)).thenReturn(true);
        when(redisTokenStorage.getUserFromEmail(vDto.getEmail())).thenReturn(detailsDto);
        when(userService.save(detailsDto)).thenReturn(savedUser);
        when(jwtUtils.generateAccessTokenFromUser(savedUser)).thenReturn(mockJwt);

        LoginResponseDTO result = signInService.verifyCode(vDto, response);

        assertNotNull(result);
        assertEquals(mockJwt, result.getToken());
        verify(redisTokenStorage).deleteUserFromEmail(vDto.getEmail());
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("VerifyCode - Fail: Invalid or expired code")
    void verifyCode_ShouldThrowException_WhenCodeIsInvalid() {
        VerificationDTO vDto = new VerificationDTO("test@example.com", 999999);
        when(redisTokenStorage.checkStorage(vDto)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> signInService.verifyCode(vDto, response));
        verify(userService, never()).save(any());
    }

    @Test
    @DisplayName("ResendAndStore - Success: Update Redis and Republish")
    void resendAndStore_ShouldCallRedisAndKafka() {
        String email = "test@example.com";
        VerificationDTO vDto = new VerificationDTO(email, 654321);

        when(redisTokenStorage.resendEmailAndStore(email)).thenReturn(vDto);

        signInService.resendAndStore(email);

        verify(redisTokenStorage).resendEmailAndStore(email);
        verify(kafkaCodePublisher).publishEmail(vDto);
    }
}