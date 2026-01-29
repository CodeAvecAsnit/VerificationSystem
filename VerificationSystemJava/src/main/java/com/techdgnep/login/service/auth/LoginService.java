package com.techdgnep.login.service.auth;

import com.techdgnep.login.data.dto.LoginResponseDTO;
import com.techdgnep.login.data.dto.SignInDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    LoginResponseDTO login(SignInDTO signIn, HttpServletResponse httpResponse);
}
