package com.techdgnep.login.service.auth.impl;

import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import com.techdgnep.login.data.repository.AppUserRepository;
import com.techdgnep.login.service.auth.SignInService;
import com.techdgnep.login.service.kafka.KafkaCodePublisher;
import com.techdgnep.login.service.redis.RedisTokenStorage;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl implements SignInService {
    private final AppUserRepository appUserRepository;
    private final KafkaCodePublisher publisher;
    private final RedisTokenStorage redisTokenStorage;

    public SignInServiceImpl(AppUserRepository appUserRepository, KafkaCodePublisher publisher, RedisTokenStorage redisTokenStorage) {
        this.appUserRepository = appUserRepository;
        this.publisher = publisher;
        this.redisTokenStorage = redisTokenStorage;
    }


    @Override
    public void verifyStoreAndSend(SignInDTO signInDTO) {

    }

    @Override
    public void verifyCode(VerificationDTO verifyData) {

    }
}
