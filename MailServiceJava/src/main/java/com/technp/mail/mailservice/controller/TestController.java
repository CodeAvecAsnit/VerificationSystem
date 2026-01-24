package com.technp.mail.mailservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/connection/test")
public class TestController {

    @GetMapping
    public ResponseEntity<Map> testConnection(){
        return ResponseEntity.ok(Map.of("test","Success"));
    }
}
