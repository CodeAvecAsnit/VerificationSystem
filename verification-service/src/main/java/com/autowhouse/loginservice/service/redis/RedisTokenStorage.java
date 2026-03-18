package com.autowhouse.loginservice.service.redis;

import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;

/**
 * @author : Asnit Bakhati
 * @Date : 11th Feb, 2026
 */
public interface RedisTokenStorage {

    VerificationDTO generateAndStore(SignInDTO signInDTO);
    boolean checkStorage(VerificationDTO verificationDTO);
    DetailsCodeDTO getUserFromEmail(String email);
    void deleteUserFromEmail(String email);
    VerificationDTO resendEmailAndStore(String email);
}
