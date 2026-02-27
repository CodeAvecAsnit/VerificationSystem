package com.autowhouse.itemservice.service.impl;

import com.autowhouse.itemservice.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public boolean test(){
        return true;
    }
}
