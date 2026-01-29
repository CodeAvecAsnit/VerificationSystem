package com.techdgnep.login.service.auth.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techdgnep.login.exception.UserAlreadyExistsException;
import com.techdgnep.login.data.database.ApplicationUser;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import com.techdgnep.login.data.repository.AppUserRepository;
import com.techdgnep.login.service.auth.SignInService;
import com.techdgnep.login.service.kafka.KafkaCodePublisher;
import com.techdgnep.login.service.redis.RedisTokenStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SignInServiceImpl implements SignInService {
    private final AppUserRepository appUserRepository;
    private final KafkaCodePublisher kafkaCodePublisher;
    private final RedisTokenStorage redisTokenStorage;

    public SignInServiceImpl(AppUserRepository appUserRepository,
                             @Qualifier("kafkaCodePublisherImpl") KafkaCodePublisher kafkaCodePublisher,
                             @Qualifier("redisTokenStorageImpl") RedisTokenStorage redisTokenStorage) {
        this.appUserRepository = appUserRepository;
        this.kafkaCodePublisher = kafkaCodePublisher;
        this.redisTokenStorage = redisTokenStorage;
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
    public void verifyCode(VerificationDTO verifyData) {
        if(redisTokenStorage.checkStorage(verifyData)){

        }


    }
}
