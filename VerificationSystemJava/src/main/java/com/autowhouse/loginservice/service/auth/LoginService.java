package com.autowhouse.loginservice.service.auth;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    LoginResponseDTO login(SignInDTO signIn, HttpServletResponse httpResponse);
}
