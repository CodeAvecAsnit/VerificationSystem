package com.techdgnep.login.service.redis;

public interface RedisTokenStorage {
    public void generateAndStore(String email);
}
