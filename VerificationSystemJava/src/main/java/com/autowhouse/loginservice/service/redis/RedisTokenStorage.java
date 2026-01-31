package com.autowhouse.loginservice.service.redis;

import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;

public interface RedisTokenStorage {

    VerificationDTO generateAndStore(SignInDTO signInDTO);
    boolean checkStorage(VerificationDTO verificationDTO);
    DetailsCodeDTO getUserFromEmail(String email);
    VerificationDTO resendEmailAndStore(String email);
}
