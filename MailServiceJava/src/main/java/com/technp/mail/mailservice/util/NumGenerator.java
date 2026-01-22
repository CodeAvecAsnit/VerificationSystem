package com.technp.mail.mailservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NumGenerator {

    @Autowired
    private Random random;

    public Integer generateEmailVerificationCode(){
        return random.nextInt(100000,1000000);
    }

}
