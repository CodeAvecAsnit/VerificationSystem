package com.autowhouse.loginservice.data.dto;

public class CodeEntry {
    private final int code;
    private final long createdAt;
    private int attempts;

    public CodeEntry(int code) {
        this.code = code;
        this.createdAt = System.currentTimeMillis();
        this.attempts = 0;
    }

    public int getCode() {
        return code;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}
