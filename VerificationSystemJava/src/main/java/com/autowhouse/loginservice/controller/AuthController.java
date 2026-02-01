package com.autowhouse.loginservice.controller;

import com.autowhouse.loginservice.data.dto.LoginResponseDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import com.autowhouse.loginservice.service.auth.SignInService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Asnit Bakhati
 * @Date : 31st Jan,2026
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final SignInService signInService;

    @Autowired
    public AuthController(@Valid @Qualifier("signInServiceImpl") SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userSignIn(@RequestBody SignInDTO userDetails){
        signInService.verifyStoreAndSend(userDetails);
    }

    @PostMapping("/verify")
    public ResponseEntity<LoginResponseDTO> userSignIn(@Valid @RequestBody
                                                           VerificationDTO verificationDTO,
                                                       HttpServletResponse response) {
       return ResponseEntity.ok(signInService.verifyCode(verificationDTO,response));
    }

    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resentConfirmationMail(@Email @RequestParam String email){
        System.out.println("Hello world");
    }
}
