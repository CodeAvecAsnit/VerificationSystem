package com.technp.mail.mailservice.dto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCodeGenerator {

    @Autowired
    private Random random;




}
