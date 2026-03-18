package com.autowhouse.loginservice.exception.custom;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
