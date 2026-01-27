package com.techdgnep.login.util;

import java.util.Random;


public class RandomCodeGenerator {
    private final Random random;

    public RandomCodeGenerator(){
        this.random = new Random();
    }

    public int GenerateCode(){
        return random.nextInt(100000,999999);
    }
}
