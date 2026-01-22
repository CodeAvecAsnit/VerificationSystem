package com.technp.mail.mailservice.controller;

import com.technp.mail.mailservice.dto.MailRequestDTO;
import com.technp.mail.mailservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send/mail")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController( @Qualifier("mailServiceImpl") MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<Integer> SendVerification(@RequestBody MailRequestDTO sender){
        return ResponseEntity.ok(mailService.SendEmail(sender.getEmail()));
    }
}
