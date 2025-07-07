package com.technp.mail.mailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Random;

@SpringBootApplication
public class MailServiceApplication {


    public static void main(String[] args) {

        SpringApplication.run(MailServiceApplication.class, args);
    }


    @Bean
    public Random random(){
        return new Random();
    }

}
