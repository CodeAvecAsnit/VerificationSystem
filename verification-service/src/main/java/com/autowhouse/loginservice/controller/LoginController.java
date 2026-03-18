package com.autowhouse.loginservice.controller;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.service.auth.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Asnit Bakhati
 * @Date : 29th Jan,2026
 */
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(@Qualifier("loginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody SignInDTO signInDTO,
                                                  HttpServletResponse response){
        return ResponseEntity.ok(loginService.login(signInDTO,response));
    }
}
