package com.autowhouse.loginservice.service.auth;


import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;

public interface SignInService {
    void verifyStoreAndSend(SignInDTO signInDTO);
    LoginResponseDTO verifyCode(VerificationDTO verifyData, HttpServletResponse response);
    void resendAndStore(String email) throws JsonProcessingException;
}
