package com.techdgnep.login.service.redis.impl;

import com.techdgnep.login.service.redis.RedisTokenStorage;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenStorageImpl implements RedisTokenStorage {
    @Override
    public void generateAndStore(String email) {

    }
}
