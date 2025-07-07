package com.technp.mail.mailservice;


import com.technp.mail.mailservice.HelperFuctions.NumGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private Logger logger = LoggerFactory.getLogger(MailService.class);


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NumGenerator numGenerator;

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
