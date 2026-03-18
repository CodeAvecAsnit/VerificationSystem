package com.autowhouse.loginservice.exception.custom;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public class MaxRateReachedException extends RuntimeException {
    public MaxRateReachedException(String message) {
        super(message);
    }
}
