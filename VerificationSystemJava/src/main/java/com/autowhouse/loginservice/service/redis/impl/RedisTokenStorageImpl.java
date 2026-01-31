package com.autowhouse.loginservice.service.redis.impl;

import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import com.autowhouse.loginservice.data.mapper.AuthMapper;
import com.autowhouse.loginservice.exception.custom.MaxRateReachedException;
import com.autowhouse.loginservice.service.redis.RedisTokenStorage;
import com.autowhouse.loginservice.util.CustomRedisMapper;
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
        int generatedCode = generateCode();
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
            return true;
        } else return false;
    }

    @Override
    public DetailsCodeDTO getUserFromEmail(String email){
        Optional<DetailsCodeDTO> detailsCodeDTO = redisCache.getOptional(email);
        if(detailsCodeDTO.isEmpty()) throw new UsernameNotFoundException("Username not found.");
            redisCache.delete(email);
            return detailsCodeDTO.get();
    }

    @Override
    public VerificationDTO resendEmailAndStore(String email) {
        DetailsCodeDTO user = getUserFromEmail(email);
        if(!user.canSendMail()) throw new MaxRateReachedException("Too Many tries. Try sending later.");
        int newCode = generateCode();
        user.setCode(newCode);
        user.incrementMailTry();
        redisCache.set(email,user,Duration.ofMinutes(5));
        return authMapper.toVerificationDTO(user);
    }

    private int generateCode() {
        return secureRandom.nextInt(100000,1000000);
    }
}
