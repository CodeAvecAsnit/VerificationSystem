package com.technp.mail.mailservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<Map<String,String>> handleAlgoFault(NoSuchAlgorithmException ex){
        return ResponseEntity.status(500).body(Map.of("Error","Internal Server Error Occurred"));
    }

}
