package com.autowhouse.loginservice.exception.custom;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) {
        super(message);
    }
}
