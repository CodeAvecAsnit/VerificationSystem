package com.techdgnep.login.controller;

import com.techdgnep.login.service.EmailPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mail/publish")
public class EmailController{

    private final EmailPublisherService emailPublisherService;

    @Autowired
    public EmailController(@Qualifier("emailPublisherServiceImpl") EmailPublisherService emailPublisherService) {
        this.emailPublisherService = emailPublisherService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void send(){
        String[] arr = new String[]{"asnitbakhati@gmail","dkwhosash@gmail.com","alsoasnit@gmail.com",
        "csit23081031_asnit@gmail.com","aranzabakhati@gmail.com"};
        for(String str : arr) {
            emailPublisherService.publishEmail(str);
        }
    }
}