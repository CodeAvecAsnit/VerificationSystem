package com.autowhouse.loginservice.controller;

import com.autowhouse.loginservice.data.dto.PasswordDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/password")
public class PasswordController {

    @PostMapping("/reset")
    public void resetPassword(@RequestBody PasswordDTO passwordDTO){
    }
}
