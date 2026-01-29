package com.techdgnep.login.service.auth.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techdgnep.login.data.dto.DetailsCodeDTO;
import com.techdgnep.login.data.dto.LoginResponseDTO;
import com.techdgnep.login.exception.UserAlreadyExistsException;
import com.techdgnep.login.data.database.ApplicationUser;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import com.techdgnep.login.data.repository.AppUserRepository;
import com.techdgnep.login.service.auth.SignInService;
import com.techdgnep.login.service.kafka.KafkaCodePublisher;
import com.techdgnep.login.service.redis.RedisTokenStorage;
import com.techdgnep.login.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SignInServiceImpl implements SignInService {
    private final AppUserRepository appUserRepository;
    private final KafkaCodePublisher kafkaCodePublisher;
    private final RedisTokenStorage redisTokenStorage;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public SignInServiceImpl(AppUserRepository appUserRepository,
                             @Qualifier("kafkaCodePublisherImpl") KafkaCodePublisher kafkaCodePublisher,
                             @Qualifier("redisTokenStorageImpl") RedisTokenStorage redisTokenStorage,
                             JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.kafkaCodePublisher = kafkaCodePublisher;
        this.redisTokenStorage = redisTokenStorage;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void verifyStoreAndSend(SignInDTO signInDTO) {
        try{
        Optional<ApplicationUser> appUser = appUserRepository.findByEmail(signInDTO.getUserName());
        if(appUser.isPresent()) throw new UserAlreadyExistsException("The user with this email already exists");
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
            ApplicationUser user =  userData.build(passwordEncoder);
            ApplicationUser savedUser = appUserRepository.save(user);
            String jwt = jwtUtils.generateAccessTokenFromUser(savedUser);
            attachJWT(response,jwt);
            return new LoginResponseDTO("Successfully created an account",jwt);
        }else throw new BadCredentialsException("the code does not match");
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
