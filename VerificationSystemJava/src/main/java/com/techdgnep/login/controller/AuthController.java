package com.techdgnep.login.controller;

import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/v1/")
public class AuthController {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userSignIn(@RequestBody SignInDTO userDetails){

    }

    @PostMapping
    public void registerUser(@RequestBody VerificationDTO request){

    }
}
