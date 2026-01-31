package com.autowhouse.loginservice.service.auth.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.exception.custom.UserAlreadyExistsException;
import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import com.autowhouse.loginservice.service.application.UserService;
import com.autowhouse.loginservice.service.auth.SignInService;
import com.autowhouse.loginservice.service.kafka.KafkaCodePublisher;
import com.autowhouse.loginservice.service.redis.RedisTokenStorage;
import com.autowhouse.loginservice.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SignInServiceImpl implements SignInService {

    private final UserService userService;
    private final KafkaCodePublisher kafkaCodePublisher;
    private final RedisTokenStorage redisTokenStorage;
    private final JwtUtils jwtUtils;

    public SignInServiceImpl(@Qualifier("kafkaCodePublisherImpl") KafkaCodePublisher kafkaCodePublisher,
                             @Qualifier("userServiceImpl") UserService userService,
                             @Qualifier("redisTokenStorageImpl") RedisTokenStorage redisTokenStorage,
                             JwtUtils jwtUtils) {
        this.userService = userService;
        this.kafkaCodePublisher = kafkaCodePublisher;
        this.redisTokenStorage = redisTokenStorage;
        this.jwtUtils = jwtUtils;;
    }


    @Override
    public void verifyStoreAndSend(SignInDTO signInDTO) {
        try{
        Optional<ApplicationUser> appUser = userService.findByEmail(signInDTO.getUserName());
        if(appUser.isPresent()) throw new UserAlreadyExistsException("User already exists.");
        VerificationDTO verificationData = redisTokenStorage.generateAndStore(signInDTO);
        kafkaCodePublisher.publishEmail(verificationData);}
        catch (JsonProcessingException jEx){
            throw new RuntimeException();
        }
    }

    @Override
    public LoginResponseDTO verifyCode(VerificationDTO verifyData, HttpServletResponse response) {
        if(redisTokenStorage.checkStorage(verifyData)){
            DetailsCodeDTO userData = redisTokenStorage.getUserFromEmail(verifyData.getEmail());
            ApplicationUser savedUser = userService.save(userData);
            String jwt = jwtUtils.generateAccessTokenFromUser(savedUser);
            attachJWT(response,jwt);
            return new LoginResponseDTO("Successfully created an account",jwt);
        }else throw new BadCredentialsException("the code does not match");
    }

    @Override
    public void resendAndStore(String email) {
        try {
            VerificationDTO verificationData = redisTokenStorage.resendEmailAndStore(email);
            kafkaCodePublisher.publishEmail(verificationData);
        }catch (JsonProcessingException jPx){
            throw new RuntimeException();
        }
    }

    public void attachJWT(HttpServletResponse response, String jwt){{
            Cookie cookie = new Cookie("access_token",jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }
}
