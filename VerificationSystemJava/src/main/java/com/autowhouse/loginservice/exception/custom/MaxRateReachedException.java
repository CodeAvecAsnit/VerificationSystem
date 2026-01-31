package com.autowhouse.loginservice.exception.custom;

public class MaxRateReachedException extends RuntimeException {
    public MaxRateReachedException(String message) {
        super(message);
    }
}
