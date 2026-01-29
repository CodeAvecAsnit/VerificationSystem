package com.techdgnep.login.service.auth;


import com.techdgnep.login.data.dto.LoginResponseDTO;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface SignInService {
    void verifyStoreAndSend(SignInDTO signInDTO);

    LoginResponseDTO verifyCode(VerificationDTO verifyData, HttpServletResponse response);
}
