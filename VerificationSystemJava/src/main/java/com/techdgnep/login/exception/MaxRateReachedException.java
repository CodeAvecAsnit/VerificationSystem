package com.techdgnep.login.exception;

public class MaxRateReachedException extends RuntimeException {
    public MaxRateReachedException(String message) {
        super(message);
    }
}
