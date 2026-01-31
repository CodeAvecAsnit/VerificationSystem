package com.autowhouse.loginservice.service.redis;

import java.util.Optional;

public interface RateLimiterService {
    Optional<Integer> getTries(String email);

    void setTries(String email, int tryNumber);

    void onSuccessRemove(String email, int tryNumber);
}
