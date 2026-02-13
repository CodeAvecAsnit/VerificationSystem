package com.autowhouse.loginservice.service.auth;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public interface SignInService {
    void verifyStoreAndSend(SignInDTO signInDTO);
    LoginResponseDTO verifyCode(VerificationDTO verifyData, HttpServletResponse response);
    void resendAndStore(String email);
}
