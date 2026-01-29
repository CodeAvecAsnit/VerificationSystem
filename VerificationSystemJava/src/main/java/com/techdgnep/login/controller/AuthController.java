package com.techdgnep.login.controller;

import com.techdgnep.login.data.dto.LoginResponseDTO;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import com.techdgnep.login.service.auth.SignInService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final SignInService signInService;

    @Autowired
    public AuthController(@Qualifier("signInServiceImpl") SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userSignIn(@RequestBody SignInDTO userDetails){
        signInService.verifyStoreAndSend(userDetails);
    }

    @PostMapping("/veriy")
    public ResponseEntity<LoginResponseDTO> userSignIn(@RequestBody VerificationDTO verificationDTO,
                                                       HttpServletResponse response) {
       return ResponseEntity.ok(signInService.verifyCode(verificationDTO,response);
    }
}
