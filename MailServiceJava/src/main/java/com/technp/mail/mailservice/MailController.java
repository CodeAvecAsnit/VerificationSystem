package com.technp.mail.mailservice;


import com.technp.mail.mailservice.HelperFuctions.MailRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mindgame")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity<Integer> SendVerification(@RequestBody MailRequestDTO sender){
        return ResponseEntity.ok(mailService.SendEmail(sender.getEmail()));
    }
}
