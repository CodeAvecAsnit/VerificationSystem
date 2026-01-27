package com.techdgnep.login.service.auth;


import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;

public interface SignInService {
    public void verifyStoreAndSend(SignInDTO signInDTO);
    public void verifyCode(VerificationDTO verifyData);
}
