package com.techdgnep.login.service.redis;

import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;

public interface RedisTokenStorage {
    VerificationDTO generateAndStore(SignInDTO signInDTO);
    boolean checkStorage(VerificationDTO verificationDTO);

}
