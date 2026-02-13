package com.autowhouse.loginservice.controller;

import com.autowhouse.loginservice.data.dto.PasswordDTO;
import com.autowhouse.loginservice.service.application.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Asnit Bakhati
 * @Date : 10th Feb,2026
 */
@RestController
@RequestMapping("/api/v1/auth/password")
public class PasswordController {

    private final UserService userService;

    @Autowired
    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordDTO passwordDTO){
        if(userService.resetPassword(passwordDTO))
            return ResponseEntity.ok(Map.of("Success","Successfully changed password"));
        else return ResponseEntity.badRequest().build();
    }
}
