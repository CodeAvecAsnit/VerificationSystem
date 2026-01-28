package com.techdgnep.login.service.auth;


import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;

public interface SignInService {
    void verifyStoreAndSend(SignInDTO signInDTO);
    void verifyCode(VerificationDTO verifyData);
}
