package com.technp.mail.mailservice.service.mail.impl;

import com.technp.mail.mailservice.dto.VerificationDTO;
import com.technp.mail.mailservice.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.app.address}")
    private String mailEmail;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(VerificationDTO verificationDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailEmail);
            message.setTo(verificationDTO.getEmail());
            message.setSubject("Verification Code");
            String body = "Your email was used to Sign up for in our website. If this was not you please secure your account."
                    + " Your code is " + verificationDTO.getCode();
            message.setText(body);
            javaMailSender.send(message);
            log.info("{} mail sent successfully",verificationDTO.getEmail());

        } catch (Exception ex) {
            log.error("Mail was not Sent :", ex.getCause());
        }
    }
}
