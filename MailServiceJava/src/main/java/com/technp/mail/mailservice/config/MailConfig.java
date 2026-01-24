package com.technp.mail.mailservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Configuration
public class MailConfig {

    @Value("${mail.app.address}")
    private String mailAddress;

    @Value("${mail.app.password}")
    private String mailPass;


    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(mailAddress);
        mailSender.setPassword(mailPass);
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }

    @Bean(name = "noFilter")
    public Set<String> noFilter(){
        Set<String> set = new HashSet<>();
        set.add("/connection/test");
        set.add("/safe/key");
        set.add("/safe/key/wow");
        return set;
    }
}
