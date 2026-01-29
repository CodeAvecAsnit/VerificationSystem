package com.techdgnep.login.service.redis.impl;

import com.techdgnep.login.data.dto.DetailsCodeDTO;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import com.techdgnep.login.data.mapper.AuthMapper;
import com.techdgnep.login.service.redis.RedisTokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;

@Component
public class RedisTokenStorageImpl implements RedisTokenStorage {

    private final SecureRandom secureRandom;
    private final CustomRedisMapper<String,DetailsCodeDTO> redisCache;
    private final AuthMapper authMapper;


    @Autowired
    public RedisTokenStorageImpl(SecureRandom secureRandom,
                                 @Qualifier("detailsCache") CustomRedisMapper<String, DetailsCodeDTO> redisCache,
                                 AuthMapper authMapper) {
        this.secureRandom = secureRandom;
        this.redisCache = redisCache;
        this.authMapper = authMapper;
    }

    @Override
    public VerificationDTO generateAndStore(SignInDTO signInDTO) {
        int generatedCode = secureRandom.nextInt(100000,1000000);
        DetailsCodeDTO detailsCodeDTO = authMapper.toDetailsCodeDTO(signInDTO,generatedCode);
        redisCache.set(signInDTO.getUserName(),detailsCodeDTO, Duration.ofMinutes(5));
        return authMapper.toVerificationDTO(detailsCodeDTO);
    }

    @Override
    public boolean checkStorage(VerificationDTO verificationDTO) {
        Optional<DetailsCodeDTO> userOptional = redisCache.getOptional(verificationDTO.getEmail());
        if(userOptional.isEmpty()) throw new UsernameNotFoundException("User may not have been " +
                "Registered or Code may have already expired.");
        DetailsCodeDTO userDetails = userOptional.get();
        if(userDetails.compareCode(verificationDTO.getCode())){
            redisCache.delete(verificationDTO.getEmail());
            return true;
        } else return false;
    }

}
