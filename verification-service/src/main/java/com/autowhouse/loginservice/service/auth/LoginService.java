package com.autowhouse.loginservice.service.auth;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author : Asnit Bakhati
 * @Date : 10th Feb,2026
 */
public interface LoginService {
    LoginResponseDTO login(SignInDTO signIn, HttpServletResponse httpResponse);
}
