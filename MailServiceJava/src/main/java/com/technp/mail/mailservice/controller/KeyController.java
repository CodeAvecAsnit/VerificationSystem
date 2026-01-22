package com.technp.mail.mailservice.controller;

import com.technp.mail.mailservice.dto.KeyDTO;
import com.technp.mail.mailservice.dto.PasswordDTO;
import com.technp.mail.mailservice.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/safe/key")
public class KeyController {

    @Value("${app.secure.apiKey}")
    private String APIKey;

    @Value("${app.secure.password}")
    private String HashedAPIPassword;

    private final KeyService keyService;

    @Autowired
    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @GetMapping
    public ResponseEntity<?> getKey(@RequestBody PasswordDTO passwordDTO){
        if(keyService.comparePassword(passwordDTO.getPassword(),HashedAPIPassword))
            return ResponseEntity.ok(new KeyDTO(APIKey));
        else return ResponseEntity.status(401).build();
    }
}
