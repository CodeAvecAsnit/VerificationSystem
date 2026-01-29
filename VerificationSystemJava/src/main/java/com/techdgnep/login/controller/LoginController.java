package com.techdgnep.login.controller;

import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.service.auth.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(@Qualifier("loginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping
    public ResponseEntity<?> login(@RequestBody SignInDTO signInDTO,
                                   HttpServletResponse response){
        return ResponseEntity.ok(loginService.login(signInDTO,response));
    }

}
