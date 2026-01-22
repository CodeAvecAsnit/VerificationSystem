package com.technp.mail.mailservice.service.impl;

import com.technp.mail.mailservice.util.NumGenerator;
import com.technp.mail.mailservice.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    private final NumGenerator numGenerator;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, NumGenerator numGenerator) {
        this.javaMailSender = javaMailSender;
        this.numGenerator = numGenerator;
    }

    @Override
    public Integer SendEmail(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("furnituremandu@gmail.com");
            message.setTo(email);
            message.setSubject("Verification Code");
            Integer code = numGenerator.generateEmailVerificationCode();
            String body = "Your email was used to Sign up for in our website. If this was not you please secure your account."
                    + " Your code is " + code;
            message.setText(body);
            javaMailSender.send(message);
            return code;


        } catch (Exception ex) {
            logger.error("Mail was not Sent :", ex.getCause());
        }
        return -1;
    }


}
