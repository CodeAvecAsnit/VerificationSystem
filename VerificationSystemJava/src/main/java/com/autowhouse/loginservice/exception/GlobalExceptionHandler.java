package com.autowhouse.loginservice.exception;

import com.autowhouse.loginservice.exception.custom.MaxRateReachedException;
import com.autowhouse.loginservice.exception.custom.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * @author : Asnit Bakhati
 * @Date : 31st Jan, 2026
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(401).body(Map.of("Error",ex.getMessage()));
    }

    @ExceptionHandler(MaxRateReachedException.class)
    public ResponseEntity<Map<String,String>> handleMaxRateReached(MaxRateReachedException ex){
        return ResponseEntity.status(429).body(Map.of("Error",ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleUserExists(UserAlreadyExistsException ex){
        return ResponseEntity.status(409).body(Map.of("Error",ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUserNotFound(UsernameNotFoundException ex){
        return ResponseEntity.notFound().build();
    }

}
