package com.technp.mail.mailservice.service.mail.impl;

import com.technp.mail.mailservice.dto.VerificationDTO;
import com.technp.mail.mailservice.service.mail.MailService;
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

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(VerificationDTO verificationDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("furnituremandu@gmail.com");
            message.setTo(verificationDTO.getEmail());
            message.setSubject("Verification Code");
            String body = "Your email was used to Sign up for in our website. If this was not you please secure your account."
                    + " Your code is " + verificationDTO.getCode();
            message.setText(body);
            javaMailSender.send(message);

        } catch (Exception ex) {
            logger.error("Mail was not Sent :", ex.getCause());
        }
    }

}
